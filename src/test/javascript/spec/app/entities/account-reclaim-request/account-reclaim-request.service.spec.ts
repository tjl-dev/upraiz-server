/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
import { AccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

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
  describe('AccountReclaimRequest Service', () => {
    let service: AccountReclaimRequestService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new AccountReclaimRequestService();
      currentDate = new Date();
      elemDefault = new AccountReclaimRequest(123, 0, currentDate, VoteCcy.XNO, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            timestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
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

      it('should create a AccountReclaimRequest', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            timestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            timestamp: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a AccountReclaimRequest', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a AccountReclaimRequest', async () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            timestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            ccy: 'BBBBBB',
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            timestamp: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a AccountReclaimRequest', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a AccountReclaimRequest', async () => {
        const patchObject = Object.assign(
          {
            amount: 1,
            timestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            active: true,
          },
          new AccountReclaimRequest()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            timestamp: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a AccountReclaimRequest', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of AccountReclaimRequest', async () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            timestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            ccy: 'BBBBBB',
            active: true,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            timestamp: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of AccountReclaimRequest', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a AccountReclaimRequest', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a AccountReclaimRequest', async () => {
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
