import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MovimientoCrediticioService from './movimiento-crediticio.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import CuentaService from '@/entities/cuenta/cuenta.service';
import { type ICuenta } from '@/shared/model/cuenta.model';
import { type IMovimientoCrediticio, MovimientoCrediticio } from '@/shared/model/movimiento-crediticio.model';
import { TipoMovimiento } from '@/shared/model/enumerations/tipo-movimiento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MovimientoCrediticioUpdate',
  setup() {
    const movimientoCrediticioService = inject('movimientoCrediticioService', () => new MovimientoCrediticioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const movimientoCrediticio: Ref<IMovimientoCrediticio> = ref(new MovimientoCrediticio());

    const cuentaService = inject('cuentaService', () => new CuentaService());

    const cuentas: Ref<ICuenta[]> = ref([]);
    const tipoMovimientoValues: Ref<string[]> = ref(Object.keys(TipoMovimiento));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMovimientoCrediticio = async movimientoCrediticioId => {
      try {
        const res = await movimientoCrediticioService().find(movimientoCrediticioId);
        res.fechaMovimiento = new Date(res.fechaMovimiento);
        movimientoCrediticio.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.movimientoCrediticioId) {
      retrieveMovimientoCrediticio(route.params.movimientoCrediticioId);
    }

    const initRelationships = () => {
      cuentaService()
        .retrieve()
        .then(res => {
          cuentas.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaMovimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      tipo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      monto: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      descripcion: {},
      referenciaExterna: {},
      cuenta: {},
    };
    const v$ = useVuelidate(validationRules, movimientoCrediticio as any);
    v$.value.$validate();

    return {
      movimientoCrediticioService,
      alertService,
      movimientoCrediticio,
      previousState,
      tipoMovimientoValues,
      isSaving,
      currentLanguage,
      cuentas,
      v$,
      ...useDateFormat({ entityRef: movimientoCrediticio }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.movimientoCrediticio.id) {
        this.movimientoCrediticioService()
          .update(this.movimientoCrediticio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('solicitudCreditoApp.movimientoCrediticio.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.movimientoCrediticioService()
          .create(this.movimientoCrediticio)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('solicitudCreditoApp.movimientoCrediticio.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
