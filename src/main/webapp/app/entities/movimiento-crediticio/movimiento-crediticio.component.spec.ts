/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import MovimientoCrediticio from './movimiento-crediticio.vue';
import MovimientoCrediticioService from './movimiento-crediticio.service';
import AlertService from '@/shared/alert/alert.service';

type MovimientoCrediticioComponentType = InstanceType<typeof MovimientoCrediticio>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MovimientoCrediticio Management Component', () => {
    let movimientoCrediticioServiceStub: SinonStubbedInstance<MovimientoCrediticioService>;
    let mountOptions: MountingOptions<MovimientoCrediticioComponentType>['global'];

    beforeEach(() => {
      movimientoCrediticioServiceStub = sinon.createStubInstance<MovimientoCrediticioService>(MovimientoCrediticioService);
      movimientoCrediticioServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          movimientoCrediticioService: () => movimientoCrediticioServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        movimientoCrediticioServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MovimientoCrediticio, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.movimientoCrediticios[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(MovimientoCrediticio, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: MovimientoCrediticioComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MovimientoCrediticio, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        movimientoCrediticioServiceStub.retrieve.reset();
        movimientoCrediticioServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        movimientoCrediticioServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.retrieve.called).toBeTruthy();
        expect(comp.movimientoCrediticios[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(movimientoCrediticioServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        movimientoCrediticioServiceStub.retrieve.reset();
        movimientoCrediticioServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(movimientoCrediticioServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.movimientoCrediticios[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(movimientoCrediticioServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        movimientoCrediticioServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMovimientoCrediticio();
        await comp.$nextTick(); // clear components

        // THEN
        expect(movimientoCrediticioServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(movimientoCrediticioServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
