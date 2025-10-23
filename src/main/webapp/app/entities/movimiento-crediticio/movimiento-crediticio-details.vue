<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="movimientoCrediticio">
        <h2 class="jh-entity-heading" data-cy="movimientoCrediticioDetailsHeading">
          <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.detail.title')"></span> {{ movimientoCrediticio.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.fechaMovimiento')"></span>
          </dt>
          <dd>
            <span v-if="movimientoCrediticio.fechaMovimiento">{{ formatDateLong(movimientoCrediticio.fechaMovimiento) }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.tipo')"></span>
          </dt>
          <dd>
            <span v-text="t$('solicitudCreditoApp.TipoMovimiento.' + movimientoCrediticio.tipo)"></span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.monto')"></span>
          </dt>
          <dd>
            <span>{{ movimientoCrediticio.monto }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.descripcion')"></span>
          </dt>
          <dd>
            <span>{{ movimientoCrediticio.descripcion }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.referenciaExterna')"></span>
          </dt>
          <dd>
            <span>{{ movimientoCrediticio.referenciaExterna }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.movimientoCrediticio.cuenta')"></span>
          </dt>
          <dd>
            <div v-if="movimientoCrediticio.cuenta">
              <router-link :to="{ name: 'CuentaView', params: { cuentaId: movimientoCrediticio.cuenta.id } }">{{
                movimientoCrediticio.cuenta.numeroCuenta
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="movimientoCrediticio.id"
          :to="{ name: 'MovimientoCrediticioEdit', params: { movimientoCrediticioId: movimientoCrediticio.id } }"
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

<script lang="ts" src="./movimiento-crediticio-details.component.ts"></script>
