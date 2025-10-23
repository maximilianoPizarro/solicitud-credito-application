<template>
  <div>
    <h2 id="page-heading" data-cy="ClienteHeading">
      <span v-text="t$('solicitudCreditoApp.cliente.home.title')" id="cliente-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('solicitudCreditoApp.cliente.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ClienteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cliente"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('solicitudCreditoApp.cliente.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && clientes && clientes.length === 0">
      <span v-text="t$('solicitudCreditoApp.cliente.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="clientes && clientes.length > 0">
      <table class="table table-striped" aria-describedby="clientes">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.numeroCliente')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.nombre')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.apellido')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.fechaNacimiento')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.tipoDocumento')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.numeroDocumento')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.email')"></span></th>
            <th scope="row"><span v-text="t$('solicitudCreditoApp.cliente.telefono')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ClienteView', params: { clienteId: cliente.id } }">{{ cliente.id }}</router-link>
            </td>
            <td>{{ cliente.numeroCliente }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.apellido }}</td>
            <td>{{ cliente.fechaNacimiento }}</td>
            <td v-text="t$('solicitudCreditoApp.TipoDocumento.' + cliente.tipoDocumento)"></td>
            <td>{{ cliente.numeroDocumento }}</td>
            <td>{{ cliente.email }}</td>
            <td>{{ cliente.telefono }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ClienteView', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ClienteEdit', params: { clienteId: cliente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(cliente)"
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
          id="solicitudCreditoApp.cliente.delete.question"
          data-cy="clienteDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-cliente-heading" v-text="t$('solicitudCreditoApp.cliente.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-cliente"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeCliente()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./cliente.component.ts"></script>
