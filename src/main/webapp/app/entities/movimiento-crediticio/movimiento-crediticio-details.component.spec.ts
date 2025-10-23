/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MovimientoCrediticioDetails from './movimiento-crediticio-details.vue';
import MovimientoCrediticioService from './movimiento-crediticio.service';
import AlertService from '@/shared/alert/alert.service';

type MovimientoCrediticioDetailsComponentType = InstanceType<typeof MovimientoCrediticioDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const movimientoCrediticioSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MovimientoCrediticio Management Detail Component', () => {
    let movimientoCrediticioServiceStub: SinonStubbedInstance<MovimientoCrediticioService>;
    let mountOptions: MountingOptions<MovimientoCrediticioDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      movimientoCrediticioServiceStub = sinon.createStubInstance<MovimientoCrediticioService>(MovimientoCrediticioService);

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
          movimientoCrediticioService: () => movimientoCrediticioServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        movimientoCrediticioServiceStub.find.resolves(movimientoCrediticioSample);
        route = {
          params: {
            movimientoCrediticioId: `${123}`,
          },
        };
        const wrapper = shallowMount(MovimientoCrediticioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.movimientoCrediticio).toMatchObject(movimientoCrediticioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        movimientoCrediticioServiceStub.find.resolves(movimientoCrediticioSample);
        const wrapper = shallowMount(MovimientoCrediticioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
