<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="solicitudCreditoApp.cuenta.home.createOrEditLabel"
          data-cy="CuentaCreateUpdateHeading"
          v-text="t$('solicitudCreditoApp.cuenta.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="cuenta.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="cuenta.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.numeroCuenta')" for="cuenta-numeroCuenta"></label>
            <input
              type="text"
              class="form-control"
              name="numeroCuenta"
              id="cuenta-numeroCuenta"
              data-cy="numeroCuenta"
              :class="{ valid: !v$.numeroCuenta.$invalid, invalid: v$.numeroCuenta.$invalid }"
              v-model="v$.numeroCuenta.$model"
              required
            />
            <div v-if="v$.numeroCuenta.$anyDirty && v$.numeroCuenta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroCuenta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.fechaApertura')" for="cuenta-fechaApertura"></label>
            <div class="d-flex">
              <input
                id="cuenta-fechaApertura"
                data-cy="fechaApertura"
                type="datetime-local"
                class="form-control"
                name="fechaApertura"
                :class="{ valid: !v$.fechaApertura.$invalid, invalid: v$.fechaApertura.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaApertura.$model)"
                @change="updateZonedDateTimeField('fechaApertura', $event)"
              />
            </div>
            <div v-if="v$.fechaApertura.$anyDirty && v$.fechaApertura.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaApertura.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.montoOtorgado')" for="cuenta-montoOtorgado"></label>
            <input
              type="number"
              class="form-control"
              name="montoOtorgado"
              id="cuenta-montoOtorgado"
              data-cy="montoOtorgado"
              :class="{ valid: !v$.montoOtorgado.$invalid, invalid: v$.montoOtorgado.$invalid }"
              v-model.number="v$.montoOtorgado.$model"
              required
            />
            <div v-if="v$.montoOtorgado.$anyDirty && v$.montoOtorgado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.montoOtorgado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.saldoPendiente')" for="cuenta-saldoPendiente"></label>
            <input
              type="number"
              class="form-control"
              name="saldoPendiente"
              id="cuenta-saldoPendiente"
              data-cy="saldoPendiente"
              :class="{ valid: !v$.saldoPendiente.$invalid, invalid: v$.saldoPendiente.$invalid }"
              v-model.number="v$.saldoPendiente.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.estado')" for="cuenta-estado"></label>
            <select
              class="form-control"
              name="estado"
              :class="{ valid: !v$.estado.$invalid, invalid: v$.estado.$invalid }"
              v-model="v$.estado.$model"
              id="cuenta-estado"
              data-cy="estado"
              required
            >
              <option
                v-for="estadoCuenta in estadoCuentaValues"
                :key="estadoCuenta"
                :value="estadoCuenta"
                :label="t$('solicitudCreditoApp.EstadoCuenta.' + estadoCuenta)"
              >
                {{ estadoCuenta }}
              </option>
            </select>
            <div v-if="v$.estado.$anyDirty && v$.estado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.estado.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.fechaCierre')" for="cuenta-fechaCierre"></label>
            <div class="d-flex">
              <input
                id="cuenta-fechaCierre"
                data-cy="fechaCierre"
                type="datetime-local"
                class="form-control"
                name="fechaCierre"
                :class="{ valid: !v$.fechaCierre.$invalid, invalid: v$.fechaCierre.$invalid }"
                :value="convertDateTimeFromServer(v$.fechaCierre.$model)"
                @change="updateZonedDateTimeField('fechaCierre', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.cliente')" for="cuenta-cliente"></label>
            <select class="form-control" id="cuenta-cliente" data-cy="cliente" name="cliente" v-model="cuenta.cliente">
              <option :value="null"></option>
              <option
                :value="cuenta.cliente && clienteOption.id === cuenta.cliente.id ? cuenta.cliente : clienteOption"
                v-for="clienteOption in clientes"
                :key="clienteOption.id"
              >
                {{ clienteOption.numeroDocumento }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.cuenta.plan')" for="cuenta-plan"></label>
            <select class="form-control" id="cuenta-plan" data-cy="plan" name="plan" v-model="cuenta.plan">
              <option :value="null"></option>
              <option
                :value="cuenta.plan && planDeCreditoOption.id === cuenta.plan.id ? cuenta.plan : planDeCreditoOption"
                v-for="planDeCreditoOption in planDeCreditos"
                :key="planDeCreditoOption.id"
              >
                {{ planDeCreditoOption.nombre }}
              </option>
            </select>
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
<script lang="ts" src="./cuenta-update.component.ts"></script>
