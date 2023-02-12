/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import VoteTargetService from '@/entities/vote-target/vote-target.service';
import { VoteTarget } from '@/shared/model/vote-target.model';
import { VoteTargetType } from '@/shared/model/enumerations/vote-target-type.model';
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
  describe('VoteTarget Service', () => {
    let service: VoteTargetService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new VoteTargetService();
      currentDate = new Date();
      elemDefault = new VoteTarget(
        123,
        'AAAAAAA',
        VoteTargetType.POST,
        0,
        VoteCcy.XNO,
        'AAAAAAA',
        false,
        false,
        currentDate,
        currentDate,
        false
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            created: dayjs(currentDate).format(DATE_TIME_FORMAT),
            expiry: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a VoteTarget', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            created: dayjs(currentDate).format(DATE_TIME_FORMAT),
            expiry: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            created: currentDate,
            expiry: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a VoteTarget', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a VoteTarget', async () => {
        const returnedFromService = Object.assign(
          {
            url: 'BBBBBB',
            votetype: 'BBBBBB',
            payout: 1,
            ccy: 'BBBBBB',
            comment: 'BBBBBB',
            active: true,
            funded: true,
            created: dayjs(currentDate).format(DATE_TIME_FORMAT),
            expiry: dayjs(currentDate).format(DATE_TIME_FORMAT),
            boosted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            expiry: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a VoteTarget', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a VoteTarget', async () => {
        const patchObject = Object.assign(
          {
            votetype: 'BBBBBB',
            ccy: 'BBBBBB',
            funded: true,
            created: dayjs(currentDate).format(DATE_TIME_FORMAT),
            boosted: true,
          },
          new VoteTarget()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            expiry: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a VoteTarget', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of VoteTarget', async () => {
        const returnedFromService = Object.assign(
          {
            url: 'BBBBBB',
            votetype: 'BBBBBB',
            payout: 1,
            ccy: 'BBBBBB',
            comment: 'BBBBBB',
            active: true,
            funded: true,
            created: dayjs(currentDate).format(DATE_TIME_FORMAT),
            expiry: dayjs(currentDate).format(DATE_TIME_FORMAT),
            boosted: true,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            created: currentDate,
            expiry: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of VoteTarget', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a VoteTarget', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a VoteTarget', async () => {
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
