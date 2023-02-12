/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/date/filters';
import VoteService from '@/entities/vote/vote.service';
import { Vote } from '@/shared/model/vote.model';

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
  describe('Vote Service', () => {
    let service: VoteService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new VoteService();
      currentDate = new Date();
      elemDefault = new Vote(123, currentDate, false, currentDate, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            votedTimestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verifiedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a Vote', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            votedTimestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verifiedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            votedTimestamp: currentDate,
            verifiedTime: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Vote', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Vote', async () => {
        const returnedFromService = Object.assign(
          {
            votedTimestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verified: true,
            verifiedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verifiedBy: 'BBBBBB',
            paid: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            votedTimestamp: currentDate,
            verifiedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Vote', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Vote', async () => {
        const patchObject = Object.assign(
          {
            votedTimestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verified: true,
            paid: true,
          },
          new Vote()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            votedTimestamp: currentDate,
            verifiedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Vote', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Vote', async () => {
        const returnedFromService = Object.assign(
          {
            votedTimestamp: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verified: true,
            verifiedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            verifiedBy: 'BBBBBB',
            paid: true,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            votedTimestamp: currentDate,
            verifiedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Vote', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Vote', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Vote', async () => {
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
