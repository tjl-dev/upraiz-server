/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import VoteUpdateComponent from '@/entities/vote/vote-update.vue';
import VoteClass from '@/entities/vote/vote-update.component';
import VoteService from '@/entities/vote/vote.service';

import VoteTargetService from '@/entities/vote-target/vote-target.service';

import VotePayoutService from '@/entities/vote-payout/vote-payout.service';

import VoterService from '@/entities/voter/voter.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Vote Management Update Component', () => {
    let wrapper: Wrapper<VoteClass>;
    let comp: VoteClass;
    let voteServiceStub: SinonStubbedInstance<VoteService>;

    beforeEach(() => {
      voteServiceStub = sinon.createStubInstance<VoteService>(VoteService);

      wrapper = shallowMount<VoteClass>(VoteUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voteService: () => voteServiceStub,
          alertService: () => new AlertService(),

          voteTargetService: () =>
            sinon.createStubInstance<VoteTargetService>(VoteTargetService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          votePayoutService: () =>
            sinon.createStubInstance<VotePayoutService>(VotePayoutService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voterService: () =>
            sinon.createStubInstance<VoterService>(VoterService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
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
        const entity = { id: 123 };
        comp.vote = entity;
        voteServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.vote = entity;
        voteServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVote = { id: 123 };
        voteServiceStub.find.resolves(foundVote);
        voteServiceStub.retrieve.resolves([foundVote]);

        // WHEN
        comp.beforeRouteEnter({ params: { voteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.vote).toBe(foundVote);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
