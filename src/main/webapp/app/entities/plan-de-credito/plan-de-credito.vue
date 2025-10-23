<template>
  <div>
    <h2 id="page-heading" data-cy="PlanDeCreditoHeading">
      <span v-text="t$('solicitudCreditoApp.planDeCredito.home.title')" id="plan-de-credito-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('solicitudCreditoApp.planDeCredito.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PlanDeCreditoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-plan-de-credito"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && planDeCreditos && planDeCreditos.length === 0">
      <span v-text="t$('solicitudCreditoApp.planDeCredito.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="planDeCreditos && planDeCreditos.length > 0">
      <table class="table table-striped" aria-describedby="planDeCreditos">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.planDeCredito.nombre')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.planDeCredito.descripcion')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.planDeCredito.tipo')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.planDeCredito.tasaInteres')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.planDeCredito.plazoMaximo')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="planDeCredito in planDeCreditos" :key="planDeCredito.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PlanDeCreditoView', params: { planDeCreditoId: planDeCredito.id } }">{{
                planDeCredito.id
              }}</router-link>
            </td>
            <td>{{ planDeCredito.nombre }}</td>
            <td>{{ planDeCredito.descripcion }}</td>
            <td v-text="t$('solicitudCreditoApp.TipoPlan.' + planDeCredito.tipo)"></td>
            <td>{{ planDeCredito.tasaInteres }}</td>
            <td>{{ planDeCredito.plazoMaximo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PlanDeCreditoView', params: { planDeCreditoId: planDeCredito.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PlanDeCreditoEdit', params: { planDeCreditoId: planDeCredito.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(planDeCredito)"
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
          id="solicitudCreditoApp.planDeCredito.delete.question"
          data-cy="planDeCreditoDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-planDeCredito-heading" v-text="t$('solicitudCreditoApp.planDeCredito.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-planDeCredito"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePlanDeCredito()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./plan-de-credito.component.ts"></script>
