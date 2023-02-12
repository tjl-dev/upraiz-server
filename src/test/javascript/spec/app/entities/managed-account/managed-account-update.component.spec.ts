/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ManagedAccountUpdateComponent from '@/entities/managed-account/managed-account-update.vue';
import ManagedAccountClass from '@/entities/managed-account/managed-account-update.component';
import ManagedAccountService from '@/entities/managed-account/managed-account.service';

import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
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
  describe('ManagedAccount Management Update Component', () => {
    let wrapper: Wrapper<ManagedAccountClass>;
    let comp: ManagedAccountClass;
    let managedAccountServiceStub: SinonStubbedInstance<ManagedAccountService>;

    beforeEach(() => {
      managedAccountServiceStub = sinon.createStubInstance<ManagedAccountService>(ManagedAccountService);

      wrapper = shallowMount<ManagedAccountClass>(ManagedAccountUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          managedAccountService: () => managedAccountServiceStub,
          alertService: () => new AlertService(),

          accountReclaimPayoutService: () =>
            sinon.createStubInstance<AccountReclaimPayoutService>(AccountReclaimPayoutService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voteManagerService: () =>
            sinon.createStubInstance<VoteManagerService>(VoteManagerService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          accountReclaimRequestService: () =>
            sinon.createStubInstance<AccountReclaimRequestService>(AccountReclaimRequestService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.managedAccount = entity;
        managedAccountServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(managedAccountServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.managedAccount = entity;
        managedAccountServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(managedAccountServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundManagedAccount = { id: 123 };
        managedAccountServiceStub.find.resolves(foundManagedAccount);
        managedAccountServiceStub.retrieve.resolves([foundManagedAccount]);

        // WHEN
        comp.beforeRouteEnter({ params: { managedAccountId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.managedAccount).toBe(foundManagedAccount);
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
