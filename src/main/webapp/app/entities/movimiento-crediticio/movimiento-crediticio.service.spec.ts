/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import MovimientoCrediticioService from './movimiento-crediticio.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { MovimientoCrediticio } from '@/shared/model/movimiento-crediticio.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('MovimientoCrediticio Service', () => {
    let service: MovimientoCrediticioService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new MovimientoCrediticioService();
      currentDate = new Date();
      elemDefault = new MovimientoCrediticio(123, currentDate, 'PAGO', 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaMovimiento: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a MovimientoCrediticio', async () => {
        const returnedFromService = { id: 123, fechaMovimiento: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { fechaMovimiento: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a MovimientoCrediticio', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a MovimientoCrediticio', async () => {
        const returnedFromService = {
          fechaMovimiento: dayjs(currentDate).format(DATE_TIME_FORMAT),
          tipo: 'BBBBBB',
          monto: 1,
          descripcion: 'BBBBBB',
          referenciaExterna: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaMovimiento: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a MovimientoCrediticio', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a MovimientoCrediticio', async () => {
        const patchObject = { monto: 1, descripcion: 'BBBBBB', referenciaExterna: 'BBBBBB', ...new MovimientoCrediticio() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaMovimiento: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a MovimientoCrediticio', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of MovimientoCrediticio', async () => {
        const returnedFromService = {
          fechaMovimiento: dayjs(currentDate).format(DATE_TIME_FORMAT),
          tipo: 'BBBBBB',
          monto: 1,
          descripcion: 'BBBBBB',
          referenciaExterna: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaMovimiento: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of MovimientoCrediticio', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a MovimientoCrediticio', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a MovimientoCrediticio', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
