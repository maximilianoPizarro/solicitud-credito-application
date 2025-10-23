/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import MovimientoCrediticioUpdate from './movimiento-crediticio-update.vue';
import MovimientoCrediticioService from './movimiento-crediticio.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

import CuentaService from '@/entities/cuenta/cuenta.service';

type MovimientoCrediticioUpdateComponentType = InstanceType<typeof MovimientoCrediticioUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const movimientoCrediticioSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MovimientoCrediticioUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MovimientoCrediticio Management Update Component', () => {
    let comp: MovimientoCrediticioUpdateComponentType;
    let movimientoCrediticioServiceStub: SinonStubbedInstance<MovimientoCrediticioService>;

    beforeEach(() => {
      route = {};
      movimientoCrediticioServiceStub = sinon.createStubInstance<MovimientoCrediticioService>(MovimientoCrediticioService);
      movimientoCrediticioServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          movimientoCrediticioService: () => movimientoCrediticioServiceStub,
          cuentaService: () =>
            sinon.createStubInstance<CuentaService>(CuentaService, {
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
        const wrapper = shallowMount(MovimientoCrediticioUpdate, { global: mountOptions });
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
        const wrapper = shallowMount(MovimientoCrediticioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.movimientoCrediticio = movimientoCrediticioSample;
        movimientoCrediticioServiceStub.update.resolves(movimientoCrediticioSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.update.calledWith(movimientoCrediticioSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        movimientoCrediticioServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MovimientoCrediticioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.movimientoCrediticio = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        movimientoCrediticioServiceStub.find.resolves(movimientoCrediticioSample);
        movimientoCrediticioServiceStub.retrieve.resolves([movimientoCrediticioSample]);

        // WHEN
        route = {
          params: {
            movimientoCrediticioId: `${movimientoCrediticioSample.id}`,
          },
        };
        const wrapper = shallowMount(MovimientoCrediticioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.movimientoCrediticio).toMatchObject(movimientoCrediticioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        movimientoCrediticioServiceStub.find.resolves(movimientoCrediticioSample);
        const wrapper = shallowMount(MovimientoCrediticioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
