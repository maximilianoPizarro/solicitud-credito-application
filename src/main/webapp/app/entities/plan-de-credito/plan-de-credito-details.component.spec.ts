/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PlanDeCreditoDetails from './plan-de-credito-details.vue';
import PlanDeCreditoService from './plan-de-credito.service';
import AlertService from '@/shared/alert/alert.service';

type PlanDeCreditoDetailsComponentType = InstanceType<typeof PlanDeCreditoDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const planDeCreditoSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PlanDeCredito Management Detail Component', () => {
    let planDeCreditoServiceStub: SinonStubbedInstance<PlanDeCreditoService>;
    let mountOptions: MountingOptions<PlanDeCreditoDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      planDeCreditoServiceStub = sinon.createStubInstance<PlanDeCreditoService>(PlanDeCreditoService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          planDeCreditoService: () => planDeCreditoServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        planDeCreditoServiceStub.find.resolves(planDeCreditoSample);
        route = {
          params: {
            planDeCreditoId: `${123}`,
          },
        };
        const wrapper = shallowMount(PlanDeCreditoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.planDeCredito).toMatchObject(planDeCreditoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        planDeCreditoServiceStub.find.resolves(planDeCreditoSample);
        const wrapper = shallowMount(PlanDeCreditoDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
