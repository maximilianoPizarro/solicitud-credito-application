import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PlanDeCreditoService from './plan-de-credito.service';
import { type IPlanDeCredito } from '@/shared/model/plan-de-credito.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PlanDeCredito',
  setup() {
    const { t: t$ } = useI18n();
    const dataUtils = useDataUtils();
    const planDeCreditoService = inject('planDeCreditoService', () => new PlanDeCreditoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const planDeCreditos: Ref<IPlanDeCredito[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrievePlanDeCreditos = async () => {
      isFetching.value = true;
      try {
        const res = await planDeCreditoService().retrieve();
        planDeCreditos.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePlanDeCreditos();
    };

    onMounted(async () => {
      await retrievePlanDeCreditos();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPlanDeCredito) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePlanDeCredito = async () => {
      try {
        await planDeCreditoService().delete(removeId.value);
        const message = t$('solicitudCreditoApp.planDeCredito.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePlanDeCreditos();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      planDeCreditos,
      handleSyncList,
      isFetching,
      retrievePlanDeCreditos,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePlanDeCredito,
      t$,
      ...dataUtils,
    };
  },
});
