<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="planDeCredito">
        <h2 class="jh-entity-heading" data-cy="planDeCreditoDetailsHeading">
          <span v-text="t$('solicitudCreditoApp.planDeCredito.detail.title')"></span> {{ planDeCredito.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.nombre')"></span>
          </dt>
          <dd>
            <span>{{ planDeCredito.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ planDeCredito.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.tipo')"></span>
          </dt>
          <dd>
            <span v-text="t$('solicitudCreditoApp.TipoPlan.' + planDeCredito.tipo)"></span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.tasaInteres')"></span>
          </dt>
          <dd>
            <span>{{ planDeCredito.tasaInteres }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.planDeCredito.plazoMaximo')"></span>
          </dt>
          <dd>
            <span>{{ planDeCredito.plazoMaximo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="planDeCredito.id"
          :to="{ name: 'PlanDeCreditoEdit', params: { planDeCreditoId: planDeCredito.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./plan-de-credito-details.component.ts"></script>
