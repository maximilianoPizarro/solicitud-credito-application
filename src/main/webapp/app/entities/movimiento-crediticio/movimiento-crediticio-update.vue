<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="solicitudCreditoApp.movimientoCrediticio.home.createOrEditLabel"
          data-cy="MovimientoCrediticioCreateUpdateHeading"
          v-text="t$('solicitudCreditoApp.movimientoCrediticio.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="movimientoCrediticio.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="movimientoCrediticio.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.fechaMovimiento')"
              for="movimiento-crediticio-fechaMovimiento"
            ></label>
            <div class="d-flex">
              <input
                id="movimiento-crediticio-fechaMovimiento"
                data-cy="fechaMovimiento"
                type="datetime-local"
                class="form-control"
                name="fechaMovimiento"
                :class="{ valid: !v$.fechaMovimiento.$invalid, invalid: v$.fechaMovimiento.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.fechaMovimiento.$model)"
                @change="updateZonedDateTimeField('fechaMovimiento', $event)"
              />
            </div>
            <div v-if="v$.fechaMovimiento.$anyDirty && v$.fechaMovimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaMovimiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.tipo')"
              for="movimiento-crediticio-tipo"
            ></label>
            <select
              class="form-control"
              name="tipo"
              :class="{ valid: !v$.tipo.$invalid, invalid: v$.tipo.$invalid }"
              v-model="v$.tipo.$model"
              id="movimiento-crediticio-tipo"
              data-cy="tipo"
              required
            >
              <option
                v-for="tipoMovimiento in tipoMovimientoValues"
                :key="tipoMovimiento"
                :value="tipoMovimiento"
                :label="t$('solicitudCreditoApp.TipoMovimiento.' + tipoMovimiento)"
              >
                {{ tipoMovimiento }}
              </option>
            </select>
            <div v-if="v$.tipo.$anyDirty && v$.tipo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.monto')"
              for="movimiento-crediticio-monto"
            ></label>
            <input
              type="number"
              class="form-control"
              name="monto"
              id="movimiento-crediticio-monto"
              data-cy="monto"
              :class="{ valid: !v$.monto.$invalid, invalid: v$.monto.$invalid }"
              v-model.number="v$.monto.$model"
              required
            />
            <div v-if="v$.monto.$anyDirty && v$.monto.$invalid">
              <small class="form-text text-danger" v-for="error of v$.monto.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.descripcion')"
              for="movimiento-crediticio-descripcion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="descripcion"
              id="movimiento-crediticio-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.referenciaExterna')"
              for="movimiento-crediticio-referenciaExterna"
            ></label>
            <input
              type="text"
              class="form-control"
              name="referenciaExterna"
              id="movimiento-crediticio-referenciaExterna"
              data-cy="referenciaExterna"
              :class="{ valid: !v$.referenciaExterna.$invalid, invalid: v$.referenciaExterna.$invalid }"
              v-model="v$.referenciaExterna.$model"
            />
            <div v-if="v$.referenciaExterna.$anyDirty && v$.referenciaExterna.$invalid">
              <small class="form-text text-danger" v-for="error of v$.referenciaExterna.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.movimientoCrediticio.cuenta')"
              for="movimiento-crediticio-cuenta"
            ></label>
            <select
              class="form-control"
              id="movimiento-crediticio-cuenta"
              data-cy="cuenta"
              name="cuenta"
              v-model="movimientoCrediticio.cuenta"
            >
              <option :value="null"></option>
              <option
                :value="
                  movimientoCrediticio.cuenta && cuentaOption.id === movimientoCrediticio.cuenta.id
                    ? movimientoCrediticio.cuenta
                    : cuentaOption
                "
                v-for="cuentaOption in cuentas"
                :key="cuentaOption.id"
              >
                {{ cuentaOption.numeroCuenta }}
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
<script lang="ts" src="./movimiento-crediticio-update.component.ts"></script>
