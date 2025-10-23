/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Cliente from './cliente.vue';
import ClienteService from './cliente.service';
import AlertService from '@/shared/alert/alert.service';

type ClienteComponentType = InstanceType<typeof Cliente>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Cliente Management Component', () => {
    let clienteServiceStub: SinonStubbedInstance<ClienteService>;
    let mountOptions: MountingOptions<ClienteComponentType>['global'];

    beforeEach(() => {
      clienteServiceStub = sinon.createStubInstance<ClienteService>(ClienteService);
      clienteServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          clienteService: () => clienteServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        clienteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Cliente, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(clienteServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.clientes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ClienteComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Cliente, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        clienteServiceStub.retrieve.reset();
        clienteServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        clienteServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCliente();
        await comp.$nextTick(); // clear components

        // THEN
        expect(clienteServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(clienteServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
