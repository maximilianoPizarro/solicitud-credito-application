<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="solicitudCreditoApp.planDeCredito.home.createOrEditLabel"
          data-cy="PlanDeCreditoCreateUpdateHeading"
          v-text="t$('solicitudCreditoApp.planDeCredito.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="planDeCredito.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="planDeCredito.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.planDeCredito.nombre')" for="plan-de-credito-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="plan-de-credito-nombre"
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
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.planDeCredito.descripcion')"
              for="plan-de-credito-descripcion"
            ></label>
            <textarea
              class="form-control"
              name="descripcion"
              id="plan-de-credito-descripcion"
              data-cy="descripcion"
              :class="{ valid: !v$.descripcion.$invalid, invalid: v$.descripcion.$invalid }"
              v-model="v$.descripcion.$model"
            ></textarea>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('solicitudCreditoApp.planDeCredito.tipo')" for="plan-de-credito-tipo"></label>
            <select
              class="form-control"
              name="tipo"
              :class="{ valid: !v$.tipo.$invalid, invalid: v$.tipo.$invalid }"
              v-model="v$.tipo.$model"
              id="plan-de-credito-tipo"
              data-cy="tipo"
              required
            >
              <option
                v-for="tipoPlan in tipoPlanValues"
                :key="tipoPlan"
                :value="tipoPlan"
                :label="t$('solicitudCreditoApp.TipoPlan.' + tipoPlan)"
              >
                {{ tipoPlan }}
              </option>
            </select>
            <div v-if="v$.tipo.$anyDirty && v$.tipo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.planDeCredito.tasaInteres')"
              for="plan-de-credito-tasaInteres"
            ></label>
            <input
              type="number"
              class="form-control"
              name="tasaInteres"
              id="plan-de-credito-tasaInteres"
              data-cy="tasaInteres"
              :class="{ valid: !v$.tasaInteres.$invalid, invalid: v$.tasaInteres.$invalid }"
              v-model.number="v$.tasaInteres.$model"
              required
            />
            <div v-if="v$.tasaInteres.$anyDirty && v$.tasaInteres.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tasaInteres.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('solicitudCreditoApp.planDeCredito.plazoMaximo')"
              for="plan-de-credito-plazoMaximo"
            ></label>
            <input
              type="number"
              class="form-control"
              name="plazoMaximo"
              id="plan-de-credito-plazoMaximo"
              data-cy="plazoMaximo"
              :class="{ valid: !v$.plazoMaximo.$invalid, invalid: v$.plazoMaximo.$invalid }"
              v-model.number="v$.plazoMaximo.$model"
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
<script lang="ts" src="./plan-de-credito-update.component.ts"></script>
