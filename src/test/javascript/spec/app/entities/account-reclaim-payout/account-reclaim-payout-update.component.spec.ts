/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import AccountReclaimPayoutUpdateComponent from '@/entities/account-reclaim-payout/account-reclaim-payout-update.vue';
import AccountReclaimPayoutClass from '@/entities/account-reclaim-payout/account-reclaim-payout-update.component';
import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';

import ManagedAccountService from '@/entities/managed-account/managed-account.service';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
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
  describe('AccountReclaimPayout Management Update Component', () => {
    let wrapper: Wrapper<AccountReclaimPayoutClass>;
    let comp: AccountReclaimPayoutClass;
    let accountReclaimPayoutServiceStub: SinonStubbedInstance<AccountReclaimPayoutService>;

    beforeEach(() => {
      accountReclaimPayoutServiceStub = sinon.createStubInstance<AccountReclaimPayoutService>(AccountReclaimPayoutService);

      wrapper = shallowMount<AccountReclaimPayoutClass>(AccountReclaimPayoutUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          accountReclaimPayoutService: () => accountReclaimPayoutServiceStub,
          alertService: () => new AlertService(),

          managedAccountService: () =>
            sinon.createStubInstance<ManagedAccountService>(ManagedAccountService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          accountReclaimRequestService: () =>
            sinon.createStubInstance<AccountReclaimRequestService>(AccountReclaimRequestService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voteManagerService: () =>
            sinon.createStubInstance<VoteManagerService>(VoteManagerService, {
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
        comp.accountReclaimPayout = entity;
        accountReclaimPayoutServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(accountReclaimPayoutServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.accountReclaimPayout = entity;
        accountReclaimPayoutServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(accountReclaimPayoutServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAccountReclaimPayout = { id: 123 };
        accountReclaimPayoutServiceStub.find.resolves(foundAccountReclaimPayout);
        accountReclaimPayoutServiceStub.retrieve.resolves([foundAccountReclaimPayout]);

        // WHEN
        comp.beforeRouteEnter({ params: { accountReclaimPayoutId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.accountReclaimPayout).toBe(foundAccountReclaimPayout);
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
