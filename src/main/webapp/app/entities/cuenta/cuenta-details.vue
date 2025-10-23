<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cuenta">
        <h2 class="jh-entity-heading" data-cy="cuentaDetailsHeading">
          <span v-text="t$('solicitudCreditoApp.cuenta.detail.title')"></span> {{ cuenta.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.numeroCuenta')"></span>
          </dt>
          <dd>
            <span>{{ cuenta.numeroCuenta }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.fechaApertura')"></span>
          </dt>
          <dd>
            <span v-if="cuenta.fechaApertura">{{ formatDateLong(cuenta.fechaApertura) }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.montoOtorgado')"></span>
          </dt>
          <dd>
            <span>{{ cuenta.montoOtorgado }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.saldoPendiente')"></span>
          </dt>
          <dd>
            <span>{{ cuenta.saldoPendiente }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.estado')"></span>
          </dt>
          <dd>
            <span v-text="t$('solicitudCreditoApp.EstadoCuenta.' + cuenta.estado)"></span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.fechaCierre')"></span>
          </dt>
          <dd>
            <span v-if="cuenta.fechaCierre">{{ formatDateLong(cuenta.fechaCierre) }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.cliente')"></span>
          </dt>
          <dd>
            <div v-if="cuenta.cliente">
              <router-link :to="{ name: 'ClienteView', params: { clienteId: cuenta.cliente.id } }">{{
                cuenta.cliente.numeroDocumento
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cuenta.plan')"></span>
          </dt>
          <dd>
            <div v-if="cuenta.plan">
              <router-link :to="{ name: 'PlanDeCreditoView', params: { planDeCreditoId: cuenta.plan.id } }">{{
                cuenta.plan.nombre
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="cuenta.id" :to="{ name: 'CuentaEdit', params: { cuentaId: cuenta.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cuenta-details.component.ts"></script>
