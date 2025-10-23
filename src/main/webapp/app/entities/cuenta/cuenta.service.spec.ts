/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import CuentaService from './cuenta.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Cuenta } from '@/shared/model/cuenta.model';

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
  describe('Cuenta Service', () => {
    let service: CuentaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CuentaService();
      currentDate = new Date();
      elemDefault = new Cuenta(123, 'AAAAAAA', currentDate, 0, 0, 'ACTIVA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaApertura: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaCierre: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
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

      it('should create a Cuenta', async () => {
        const returnedFromService = {
          id: 123,
          fechaApertura: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaCierre: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaApertura: currentDate, fechaCierre: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Cuenta', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Cuenta', async () => {
        const returnedFromService = {
          numeroCuenta: 'BBBBBB',
          fechaApertura: dayjs(currentDate).format(DATE_TIME_FORMAT),
          montoOtorgado: 1,
          saldoPendiente: 1,
          estado: 'BBBBBB',
          fechaCierre: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { fechaApertura: currentDate, fechaCierre: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Cuenta', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Cuenta', async () => {
        const patchObject = { numeroCuenta: 'BBBBBB', montoOtorgado: 1, ...new Cuenta() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaApertura: currentDate, fechaCierre: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Cuenta', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Cuenta', async () => {
        const returnedFromService = {
          numeroCuenta: 'BBBBBB',
          fechaApertura: dayjs(currentDate).format(DATE_TIME_FORMAT),
          montoOtorgado: 1,
          saldoPendiente: 1,
          estado: 'BBBBBB',
          fechaCierre: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { fechaApertura: currentDate, fechaCierre: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Cuenta', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Cuenta', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Cuenta', async () => {
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
