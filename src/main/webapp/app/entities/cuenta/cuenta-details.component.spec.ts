/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CuentaDetails from './cuenta-details.vue';
import CuentaService from './cuenta.service';
import AlertService from '@/shared/alert/alert.service';

type CuentaDetailsComponentType = InstanceType<typeof CuentaDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cuentaSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Cuenta Management Detail Component', () => {
    let cuentaServiceStub: SinonStubbedInstance<CuentaService>;
    let mountOptions: MountingOptions<CuentaDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cuentaServiceStub = sinon.createStubInstance<CuentaService>(CuentaService);

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
          cuentaService: () => cuentaServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cuentaServiceStub.find.resolves(cuentaSample);
        route = {
          params: {
            cuentaId: `${123}`,
          },
        };
        const wrapper = shallowMount(CuentaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cuenta).toMatchObject(cuentaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cuentaServiceStub.find.resolves(cuentaSample);
        const wrapper = shallowMount(CuentaDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
