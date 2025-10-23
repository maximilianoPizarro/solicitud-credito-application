import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import CuentaService from './cuenta.service';
import { useDateFormat } from '@/shared/composables';
import { type ICuenta } from '@/shared/model/cuenta.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CuentaDetails',
  setup() {
    const dateFormat = useDateFormat();
    const cuentaService = inject('cuentaService', () => new CuentaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const cuenta: Ref<ICuenta> = ref({});

    const retrieveCuenta = async cuentaId => {
      try {
        const res = await cuentaService().find(cuentaId);
        cuenta.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.cuentaId) {
      retrieveCuenta(route.params.cuentaId);
    }

    return {
      ...dateFormat,
      alertService,
      cuenta,

      previousState,
      t$: useI18n().t,
    };
  },
});
