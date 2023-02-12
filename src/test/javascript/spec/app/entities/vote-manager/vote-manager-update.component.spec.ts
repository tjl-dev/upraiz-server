/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import VoteManagerUpdateComponent from '@/entities/vote-manager/vote-manager-update.vue';
import VoteManagerClass from '@/entities/vote-manager/vote-manager-update.component';
import VoteManagerService from '@/entities/vote-manager/vote-manager.service';

import VoteManagerPreferencesService from '@/entities/vote-manager-preferences/vote-manager-preferences.service';

import ManagedAccountService from '@/entities/managed-account/managed-account.service';

import VoteTargetService from '@/entities/vote-target/vote-target.service';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';

import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';
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
  describe('VoteManager Management Update Component', () => {
    let wrapper: Wrapper<VoteManagerClass>;
    let comp: VoteManagerClass;
    let voteManagerServiceStub: SinonStubbedInstance<VoteManagerService>;

    beforeEach(() => {
      voteManagerServiceStub = sinon.createStubInstance<VoteManagerService>(VoteManagerService);

      wrapper = shallowMount<VoteManagerClass>(VoteManagerUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voteManagerService: () => voteManagerServiceStub,
          alertService: () => new AlertService(),

          voteManagerPreferencesService: () =>
            sinon.createStubInstance<VoteManagerPreferencesService>(VoteManagerPreferencesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          managedAccountService: () =>
            sinon.createStubInstance<ManagedAccountService>(ManagedAccountService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voteTargetService: () =>
            sinon.createStubInstance<VoteTargetService>(VoteTargetService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          accountReclaimRequestService: () =>
            sinon.createStubInstance<AccountReclaimRequestService>(AccountReclaimRequestService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          accountReclaimPayoutService: () =>
            sinon.createStubInstance<AccountReclaimPayoutService>(AccountReclaimPayoutService, {
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
        comp.voteManager = entity;
        voteManagerServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteManagerServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.voteManager = entity;
        voteManagerServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteManagerServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteManager = { id: 123 };
        voteManagerServiceStub.find.resolves(foundVoteManager);
        voteManagerServiceStub.retrieve.resolves([foundVoteManager]);

        // WHEN
        comp.beforeRouteEnter({ params: { voteManagerId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voteManager).toBe(foundVoteManager);
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
