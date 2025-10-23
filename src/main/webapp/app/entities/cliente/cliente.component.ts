import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ClienteService from './cliente.service';
import { type ICliente } from '@/shared/model/cliente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Cliente',
  setup() {
    const { t: t$ } = useI18n();
    const clienteService = inject('clienteService', () => new ClienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const clientes: Ref<ICliente[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveClientes = async () => {
      isFetching.value = true;
      try {
        const res = await clienteService().retrieve();
        clientes.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveClientes();
    };

    onMounted(async () => {
      await retrieveClientes();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICliente) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCliente = async () => {
      try {
        await clienteService().delete(removeId.value);
        const message = t$('solicitudCreditoApp.cliente.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveClientes();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      clientes,
      handleSyncList,
      isFetching,
      retrieveClientes,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCliente,
      t$,
    };
  },
});
