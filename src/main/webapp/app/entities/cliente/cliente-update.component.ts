import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ClienteService from './cliente.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Cliente, type ICliente } from '@/shared/model/cliente.model';
import { TipoDocumento } from '@/shared/model/enumerations/tipo-documento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ClienteUpdate',
  setup() {
    const clienteService = inject('clienteService', () => new ClienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const cliente: Ref<ICliente> = ref(new Cliente());
    const tipoDocumentoValues: Ref<string[]> = ref(Object.keys(TipoDocumento));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCliente = async clienteId => {
      try {
        const res = await clienteService().find(clienteId);
        cliente.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.clienteId) {
      retrieveCliente(route.params.clienteId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      numeroCliente: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      apellido: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      fechaNacimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      tipoDocumento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      numeroDocumento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      email: {},
      telefono: {},
      cuentas: {},
    };
    const v$ = useVuelidate(validationRules, cliente as any);
    v$.value.$validate();

    return {
      clienteService,
      alertService,
      cliente,
      previousState,
      tipoDocumentoValues,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.cliente.id) {
        this.clienteService()
          .update(this.cliente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('solicitudCreditoApp.cliente.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.clienteService()
          .create(this.cliente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('solicitudCreditoApp.cliente.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
