/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PlanDeCreditoUpdate from './plan-de-credito-update.vue';
import PlanDeCreditoService from './plan-de-credito.service';
import AlertService from '@/shared/alert/alert.service';

type PlanDeCreditoUpdateComponentType = InstanceType<typeof PlanDeCreditoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planDeCreditoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PlanDeCreditoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PlanDeCredito Management Update Component', () => {
    let comp: PlanDeCreditoUpdateComponentType;
    let planDeCreditoServiceStub: SinonStubbedInstance<PlanDeCreditoService>;

    beforeEach(() => {
      route = {};
      planDeCreditoServiceStub = sinon.createStubInstance<PlanDeCreditoService>(PlanDeCreditoService);
      planDeCreditoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          planDeCreditoService: () => planDeCreditoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(PlanDeCreditoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planDeCredito = planDeCreditoSample;
        planDeCreditoServiceStub.update.resolves(planDeCreditoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planDeCreditoServiceStub.update.calledWith(planDeCreditoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        planDeCreditoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PlanDeCreditoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.planDeCredito = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(planDeCreditoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        planDeCreditoServiceStub.find.resolves(planDeCreditoSample);
        planDeCreditoServiceStub.retrieve.resolves([planDeCreditoSample]);

        // WHEN
        route = {
          params: {
            planDeCreditoId: `${planDeCreditoSample.id}`,
          },
        };
        const wrapper = shallowMount(PlanDeCreditoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.planDeCredito).toMatchObject(planDeCreditoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planDeCreditoServiceStub.find.resolves(planDeCreditoSample);
        const wrapper = shallowMount(PlanDeCreditoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
