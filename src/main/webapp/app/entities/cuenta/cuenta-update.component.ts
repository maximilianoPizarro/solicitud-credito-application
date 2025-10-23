import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import CuentaService from './cuenta.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import { type ICliente } from '@/shared/model/cliente.model';
import PlanDeCreditoService from '@/entities/plan-de-credito/plan-de-credito.service';
import { type IPlanDeCredito } from '@/shared/model/plan-de-credito.model';
import { Cuenta, type ICuenta } from '@/shared/model/cuenta.model';
import { EstadoCuenta } from '@/shared/model/enumerations/estado-cuenta.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CuentaUpdate',
  setup() {
    const cuentaService = inject('cuentaService', () => new CuentaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cuenta: Ref<ICuenta> = ref(new Cuenta());

    const clienteService = inject('clienteService', () => new ClienteService());

    const clientes: Ref<ICliente[]> = ref([]);

    const planDeCreditoService = inject('planDeCreditoService', () => new PlanDeCreditoService());

    const planDeCreditos: Ref<IPlanDeCredito[]> = ref([]);
    const estadoCuentaValues: Ref<string[]> = ref(Object.keys(EstadoCuenta));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCuenta = async cuentaId => {
      try {
        const res = await cuentaService().find(cuentaId);
        res.fechaApertura = new Date(res.fechaApertura);
        res.fechaCierre = new Date(res.fechaCierre);
        cuenta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cuentaId) {
      retrieveCuenta(route.params.cuentaId);
    }

    const initRelationships = () => {
      clienteService()
        .retrieve()
        .then(res => {
          clientes.value = res.data;
        });
      planDeCreditoService()
        .retrieve()
        .then(res => {
          planDeCreditos.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      numeroCuenta: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fechaApertura: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      montoOtorgado: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      saldoPendiente: {},
      estado: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fechaCierre: {},
      movimientos: {},
      cliente: {},
      plan: {},
    };
    const v$ = useVuelidate(validationRules, cuenta as any);
    v$.value.$validate();

    return {
      cuentaService,
      alertService,
      cuenta,
      previousState,
      estadoCuentaValues,
      isSaving,
      currentLanguage,
      clientes,
      planDeCreditos,
      v$,
      ...useDateFormat({ entityRef: cuenta }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cuenta.id) {
        this.cuentaService()
          .update(this.cuenta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('solicitudCreditoApp.cuenta.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.cuentaService()
          .create(this.cuenta)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('solicitudCreditoApp.cuenta.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
