/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import VotePayoutService from '@/entities/vote-payout/vote-payout.service';
import { VotePayout } from '@/shared/model/vote-payout.model';
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
  describe('VotePayout Service', () => {
    let service: VotePayoutService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new VotePayoutService();
      currentDate = new Date();
      elemDefault = new VotePayout(123, currentDate, 0, VoteCcy.XNO);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            paidTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a VotePayout', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            paidTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            paidTime: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a VotePayout', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a VotePayout', async () => {
        const returnedFromService = Object.assign(
          {
            paidTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            paidAmount: 1,
            paidCcy: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            paidTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a VotePayout', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a VotePayout', async () => {
        const patchObject = Object.assign(
          {
            paidAmount: 1,
            paidCcy: 'BBBBBB',
          },
          new VotePayout()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            paidTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a VotePayout', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of VotePayout', async () => {
        const returnedFromService = Object.assign(
          {
            paidTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            paidAmount: 1,
            paidCcy: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            paidTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of VotePayout', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a VotePayout', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a VotePayout', async () => {
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
