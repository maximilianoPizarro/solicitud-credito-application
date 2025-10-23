<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="solicitudCreditoApp.cliente.home.createOrEditLabel"
          data-cy="ClienteCreateUpdateHeading"
          v-text="t$('solicitudCreditoApp.cliente.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cliente.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cliente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.numeroCliente')" for="cliente-numeroCliente"></label>
            <input
              type="number"
              class="form-control"
              name="numeroCliente"
              id="cliente-numeroCliente"
              data-cy="numeroCliente"
              :class="{ valid: !v$.numeroCliente.$invalid, invalid: v$.numeroCliente.$invalid }"
              v-model.number="v$.numeroCliente.$model"
              required
            />
            <div v-if="v$.numeroCliente.$anyDirty && v$.numeroCliente.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroCliente.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.nombre')" for="cliente-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="cliente-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.apellido')" for="cliente-apellido"></label>
            <input
              type="text"
              class="form-control"
              name="apellido"
              id="cliente-apellido"
              data-cy="apellido"
              :class="{ valid: !v$.apellido.$invalid, invalid: v$.apellido.$invalid }"
              v-model="v$.apellido.$model"
              required
            />
            <div v-if="v$.apellido.$anyDirty && v$.apellido.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellido.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.cliente.fechaNacimiento')"
              for="cliente-fechaNacimiento"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="cliente-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="cliente-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaNacimiento.$anyDirty && v$.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaNacimiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.tipoDocumento')" for="cliente-tipoDocumento"></label>
            <select
              class="form-control"
              name="tipoDocumento"
              :class="{ valid: !v$.tipoDocumento.$invalid, invalid: v$.tipoDocumento.$invalid }"
              v-model="v$.tipoDocumento.$model"
              id="cliente-tipoDocumento"
              data-cy="tipoDocumento"
              required
            >
              <option
                v-for="tipoDocumento in tipoDocumentoValues"
                :key="tipoDocumento"
                :value="tipoDocumento"
                :label="t$('solicitudCreditoApp.TipoDocumento.' + tipoDocumento)"
              >
                {{ tipoDocumento }}
              </option>
            </select>
            <div v-if="v$.tipoDocumento.$anyDirty && v$.tipoDocumento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoDocumento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.cliente.numeroDocumento')"
              for="cliente-numeroDocumento"
            ></label>
            <input
              type="text"
              class="form-control"
              name="numeroDocumento"
              id="cliente-numeroDocumento"
              data-cy="numeroDocumento"
              :class="{ valid: !v$.numeroDocumento.$invalid, invalid: v$.numeroDocumento.$invalid }"
              v-model="v$.numeroDocumento.$model"
              required
            />
            <div v-if="v$.numeroDocumento.$anyDirty && v$.numeroDocumento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroDocumento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.email')" for="cliente-email"></label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="cliente-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cliente.telefono')" for="cliente-telefono"></label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="cliente-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./cliente-update.component.ts"></script>
