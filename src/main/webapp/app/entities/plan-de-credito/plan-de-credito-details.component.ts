import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PlanDeCreditoService from './plan-de-credito.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPlanDeCredito } from '@/shared/model/plan-de-credito.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PlanDeCreditoDetails',
  setup() {
    const planDeCreditoService = inject('planDeCreditoService', () => new PlanDeCreditoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const planDeCredito: Ref<IPlanDeCredito> = ref({});

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

    return {
      alertService,
      planDeCredito,

      ...dataUtils,

      previousState,
      t$: useI18n().t,
    };
  },
});
