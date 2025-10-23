<template>
  <div>
    <h2 id="page-heading" data-cy="MovimientoCrediticioHeading">
      <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.home.title')" id="movimiento-crediticio-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MovimientoCrediticioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-movimiento-crediticio"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && movimientoCrediticios && movimientoCrediticios.length === 0">
      <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="movimientoCrediticios && movimientoCrediticios.length > 0">
      <table class="table table-striped" aria-describedby="movimientoCrediticios">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaMovimiento')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.fechaMovimiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaMovimiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tipo')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.tipo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tipo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('monto')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.monto')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'monto'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('descripcion')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.descripcion')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'descripcion'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('referenciaExterna')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.referenciaExterna')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'referenciaExterna'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cuenta.numeroCuenta')">
              <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.cuenta')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cuenta.numeroCuenta'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="movimientoCrediticio in movimientoCrediticios" :key="movimientoCrediticio.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MovimientoCrediticioView', params: { movimientoCrediticioId: movimientoCrediticio.id } }">{{
                movimientoCrediticio.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(movimientoCrediticio.fechaMovimiento) || '' }}</td>
            <td v-text="t$('solicitudCreditoApp.TipoMovimiento.' + movimientoCrediticio.tipo)"></td>
            <td>{{ movimientoCrediticio.monto }}</td>
            <td>{{ movimientoCrediticio.descripcion }}</td>
            <td>{{ movimientoCrediticio.referenciaExterna }}</td>
            <td>
              <div v-if="movimientoCrediticio.cuenta">
                <router-link :to="{ name: 'CuentaView', params: { cuentaId: movimientoCrediticio.cuenta.id } }">{{
                  movimientoCrediticio.cuenta.numeroCuenta
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'MovimientoCrediticioView', params: { movimientoCrediticioId: movimientoCrediticio.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'MovimientoCrediticioEdit', params: { movimientoCrediticioId: movimientoCrediticio.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(movimientoCrediticio)"
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
        <span
          id="solicitudCreditoApp.movimientoCrediticio.delete.question"
          data-cy="movimientoCrediticioDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-movimientoCrediticio-heading"
          v-text="t$('solicitudCreditoApp.movimientoCrediticio.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-movimientoCrediticio"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeMovimientoCrediticio()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="movimientoCrediticios && movimientoCrediticios.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./movimiento-crediticio.component.ts"></script>
