<template>
  <div>
    <h2 id="page-heading" data-cy="CuentaHeading">
      <span v-text="t$('solicitudCreditoApp.cuenta.home.title')" id="cuenta-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('solicitudCreditoApp.cuenta.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'CuentaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cuenta"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('solicitudCreditoApp.cuenta.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cuentas && cuentas.length === 0">
      <span v-text="t$('solicitudCreditoApp.cuenta.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="cuentas && cuentas.length > 0">
      <table class="table table-striped" aria-describedby="cuentas">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('numeroCuenta')">
              <span v-text="t$('solicitudCreditoApp.cuenta.numeroCuenta')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroCuenta'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaApertura')">
              <span v-text="t$('solicitudCreditoApp.cuenta.fechaApertura')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaApertura'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('montoOtorgado')">
              <span v-text="t$('solicitudCreditoApp.cuenta.montoOtorgado')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'montoOtorgado'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('saldoPendiente')">
              <span v-text="t$('solicitudCreditoApp.cuenta.saldoPendiente')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'saldoPendiente'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('estado')">
              <span v-text="t$('solicitudCreditoApp.cuenta.estado')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'estado'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaCierre')">
              <span v-text="t$('solicitudCreditoApp.cuenta.fechaCierre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaCierre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cliente.numeroDocumento')">
              <span v-text="t$('solicitudCreditoApp.cuenta.cliente')"></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'cliente.numeroDocumento'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('plan.nombre')">
              <span v-text="t$('solicitudCreditoApp.cuenta.plan')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'plan.nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cuenta in cuentas" :key="cuenta.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CuentaView', params: { cuentaId: cuenta.id } }">{{ cuenta.id }}</router-link>
            </td>
            <td>{{ cuenta.numeroCuenta }}</td>
            <td>{{ formatDateShort(cuenta.fechaApertura) || '' }}</td>
            <td>{{ cuenta.montoOtorgado }}</td>
            <td>{{ cuenta.saldoPendiente }}</td>
            <td v-text="t$('solicitudCreditoApp.EstadoCuenta.' + cuenta.estado)"></td>
            <td>{{ formatDateShort(cuenta.fechaCierre) || '' }}</td>
            <td>
              <div v-if="cuenta.cliente">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: cuenta.cliente.id } }">{{
                  cuenta.cliente.numeroDocumento
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="cuenta.plan">
                <router-link :to="{ name: 'PlanDeCreditoView', params: { planDeCreditoId: cuenta.plan.id } }">{{
                  cuenta.plan.nombre
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CuentaView', params: { cuentaId: cuenta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CuentaEdit', params: { cuentaId: cuenta.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cuenta)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="solicitudCreditoApp.cuenta.delete.question" data-cy="cuentaDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cuenta-heading" v-text="t$('solicitudCreditoApp.cuenta.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cuenta"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCuenta()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="cuentas && cuentas.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cuenta.component.ts"></script>
