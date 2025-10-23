/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import CuentaUpdate from './cuenta-update.vue';
import CuentaService from './cuenta.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import ClienteService from '@/entities/cliente/cliente.service';
import PlanDeCreditoService from '@/entities/plan-de-credito/plan-de-credito.service';

type CuentaUpdateComponentType = InstanceType<typeof CuentaUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cuentaSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CuentaUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Cuenta Management Update Component', () => {
    let comp: CuentaUpdateComponentType;
    let cuentaServiceStub: SinonStubbedInstance<CuentaService>;

    beforeEach(() => {
      route = {};
      cuentaServiceStub = sinon.createStubInstance<CuentaService>(CuentaService);
      cuentaServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          cuentaService: () => cuentaServiceStub,
          clienteService: () =>
            sinon.createStubInstance<ClienteService>(ClienteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          planDeCreditoService: () =>
            sinon.createStubInstance<PlanDeCreditoService>(PlanDeCreditoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(CuentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CuentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cuenta = cuentaSample;
        cuentaServiceStub.update.resolves(cuentaSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cuentaServiceStub.update.calledWith(cuentaSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        cuentaServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CuentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.cuenta = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cuentaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        cuentaServiceStub.find.resolves(cuentaSample);
        cuentaServiceStub.retrieve.resolves([cuentaSample]);

        // WHEN
        route = {
          params: {
            cuentaId: `${cuentaSample.id}`,
          },
        };
        const wrapper = shallowMount(CuentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.cuenta).toMatchObject(cuentaSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cuentaServiceStub.find.resolves(cuentaSample);
        const wrapper = shallowMount(CuentaUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
