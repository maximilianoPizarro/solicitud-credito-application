/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PlanDeCredito from './plan-de-credito.vue';
import PlanDeCreditoService from './plan-de-credito.service';
import AlertService from '@/shared/alert/alert.service';

type PlanDeCreditoComponentType = InstanceType<typeof PlanDeCredito>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PlanDeCredito Management Component', () => {
    let planDeCreditoServiceStub: SinonStubbedInstance<PlanDeCreditoService>;
    let mountOptions: MountingOptions<PlanDeCreditoComponentType>['global'];

    beforeEach(() => {
      planDeCreditoServiceStub = sinon.createStubInstance<PlanDeCreditoService>(PlanDeCreditoService);
      planDeCreditoServiceStub.retrieve.resolves({ headers: {} });

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
          planDeCreditoService: () => planDeCreditoServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        planDeCreditoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PlanDeCredito, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(planDeCreditoServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.planDeCreditos[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PlanDeCreditoComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PlanDeCredito, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        planDeCreditoServiceStub.retrieve.reset();
        planDeCreditoServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        planDeCreditoServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePlanDeCredito();
        await comp.$nextTick(); // clear components

        // THEN
        expect(planDeCreditoServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(planDeCreditoServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
