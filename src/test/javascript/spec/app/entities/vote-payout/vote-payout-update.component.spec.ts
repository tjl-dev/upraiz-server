/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import VotePayoutUpdateComponent from '@/entities/vote-payout/vote-payout-update.vue';
import VotePayoutClass from '@/entities/vote-payout/vote-payout-update.component';
import VotePayoutService from '@/entities/vote-payout/vote-payout.service';

import VoterAccountService from '@/entities/voter-account/voter-account.service';

import VoteService from '@/entities/vote/vote.service';
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
  describe('VotePayout Management Update Component', () => {
    let wrapper: Wrapper<VotePayoutClass>;
    let comp: VotePayoutClass;
    let votePayoutServiceStub: SinonStubbedInstance<VotePayoutService>;

    beforeEach(() => {
      votePayoutServiceStub = sinon.createStubInstance<VotePayoutService>(VotePayoutService);

      wrapper = shallowMount<VotePayoutClass>(VotePayoutUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          votePayoutService: () => votePayoutServiceStub,
          alertService: () => new AlertService(),

          voterAccountService: () =>
            sinon.createStubInstance<VoterAccountService>(VoterAccountService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voteService: () =>
            sinon.createStubInstance<VoteService>(VoteService, {
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
        comp.votePayout = entity;
        votePayoutServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(votePayoutServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.votePayout = entity;
        votePayoutServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(votePayoutServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVotePayout = { id: 123 };
        votePayoutServiceStub.find.resolves(foundVotePayout);
        votePayoutServiceStub.retrieve.resolves([foundVotePayout]);

        // WHEN
        comp.beforeRouteEnter({ params: { votePayoutId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.votePayout).toBe(foundVotePayout);
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
