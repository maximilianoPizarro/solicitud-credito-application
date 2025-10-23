import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PlanDeCreditoService from './plan-de-credito.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPlanDeCredito, PlanDeCredito } from '@/shared/model/plan-de-credito.model';
import { TipoPlan } from '@/shared/model/enumerations/tipo-plan.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PlanDeCreditoUpdate',
  setup() {
    const planDeCreditoService = inject('planDeCreditoService', () => new PlanDeCreditoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const planDeCredito: Ref<IPlanDeCredito> = ref(new PlanDeCredito());
    const tipoPlanValues: Ref<string[]> = ref(Object.keys(TipoPlan));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePlanDeCredito = async planDeCreditoId => {
      try {
        const res = await planDeCreditoService().find(planDeCreditoId);
        planDeCredito.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.planDeCreditoId) {
      retrievePlanDeCredito(route.params.planDeCreditoId);
    }

    const initRelationships = () => {};

    initRelationships();

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
      tipo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      tasaInteres: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      plazoMaximo: {},
      cuentas: {},
    };
    const v$ = useVuelidate(validationRules, planDeCredito as any);
    v$.value.$validate();

    return {
      planDeCreditoService,
      alertService,
      planDeCredito,
      previousState,
      tipoPlanValues,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.planDeCredito.id) {
        this.planDeCreditoService()
          .update(this.planDeCredito)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('solicitudCreditoApp.planDeCredito.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.planDeCreditoService()
          .create(this.planDeCredito)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('solicitudCreditoApp.planDeCredito.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
