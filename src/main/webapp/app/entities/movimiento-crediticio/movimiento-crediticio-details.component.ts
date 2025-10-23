import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MovimientoCrediticioService from './movimiento-crediticio.service';
import { useDateFormat } from '@/shared/composables';
import { type IMovimientoCrediticio } from '@/shared/model/movimiento-crediticio.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MovimientoCrediticioDetails',
  setup() {
    const dateFormat = useDateFormat();
    const movimientoCrediticioService = inject('movimientoCrediticioService', () => new MovimientoCrediticioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const movimientoCrediticio: Ref<IMovimientoCrediticio> = ref({});

    const retrieveMovimientoCrediticio = async movimientoCrediticioId => {
      try {
        const res = await movimientoCrediticioService().find(movimientoCrediticioId);
        movimientoCrediticio.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.movimientoCrediticioId) {
      retrieveMovimientoCrediticio(route.params.movimientoCrediticioId);
    }

    return {
      ...dateFormat,
      alertService,
      movimientoCrediticio,

      previousState,
      t$: useI18n().t,
    };
  },
});
