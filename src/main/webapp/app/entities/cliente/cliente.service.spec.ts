/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ClienteService from './cliente.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { Cliente } from '@/shared/model/cliente.model';

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
  describe('Cliente Service', () => {
    let service: ClienteService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ClienteService();
      currentDate = new Date();
      elemDefault = new Cliente(123, 0, 'AAAAAAA', 'AAAAAAA', currentDate, 'DNI', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a Cliente', async () => {
        const returnedFromService = { id: 123, fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { fechaNacimiento: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Cliente', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Cliente', async () => {
        const returnedFromService = {
          numeroCliente: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          tipoDocumento: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          email: 'BBBBBB',
          telefono: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Cliente', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Cliente', async () => {
        const patchObject = {
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          tipoDocumento: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          email: 'BBBBBB',
          telefono: 'BBBBBB',
          ...new Cliente(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Cliente', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Cliente', async () => {
        const returnedFromService = {
          numeroCliente: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          fechaNacimiento: dayjs(currentDate).format(DATE_FORMAT),
          tipoDocumento: 'BBBBBB',
          numeroDocumento: 'BBBBBB',
          email: 'BBBBBB',
          telefono: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { fechaNacimiento: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Cliente', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Cliente', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Cliente', async () => {
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
