<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cliente">
        <h2 class="jh-entity-heading" data-cy="clienteDetailsHeading">
          <span v-text="t$('solicitudCreditoApp.cliente.detail.title')"></span> {{ cliente.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.numeroCliente')"></span>
          </dt>
          <dd>
            <span>{{ cliente.numeroCliente }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.nombre')"></span>
          </dt>
          <dd>
            <span>{{ cliente.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.apellido')"></span>
          </dt>
          <dd>
            <span>{{ cliente.apellido }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.fechaNacimiento')"></span>
          </dt>
          <dd>
            <span>{{ cliente.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.tipoDocumento')"></span>
          </dt>
          <dd>
            <span v-text="t$('solicitudCreditoApp.TipoDocumento.' + cliente.tipoDocumento)"></span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.numeroDocumento')"></span>
          </dt>
          <dd>
            <span>{{ cliente.numeroDocumento }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.email')"></span>
          </dt>
          <dd>
            <span>{{ cliente.email }}</span>
          </dd>
          <dt>
            <span v-text="t$('solicitudCreditoApp.cliente.telefono')"></span>
          </dt>
          <dd>
            <span>{{ cliente.telefono }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="cliente.id" :to="{ name: 'ClienteEdit', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cliente-details.component.ts"></script>
