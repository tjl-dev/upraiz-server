/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AccountReclaimPayoutDetailComponent from '@/entities/account-reclaim-payout/account-reclaim-payout-details.vue';
import AccountReclaimPayoutClass from '@/entities/account-reclaim-payout/account-reclaim-payout-details.component';
import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('AccountReclaimPayout Management Detail Component', () => {
    let wrapper: Wrapper<AccountReclaimPayoutClass>;
    let comp: AccountReclaimPayoutClass;
    let accountReclaimPayoutServiceStub: SinonStubbedInstance<AccountReclaimPayoutService>;

    beforeEach(() => {
      accountReclaimPayoutServiceStub = sinon.createStubInstance<AccountReclaimPayoutService>(AccountReclaimPayoutService);

      wrapper = shallowMount<AccountReclaimPayoutClass>(AccountReclaimPayoutDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { accountReclaimPayoutService: () => accountReclaimPayoutServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAccountReclaimPayout = { id: 123 };
        accountReclaimPayoutServiceStub.find.resolves(foundAccountReclaimPayout);

        // WHEN
        comp.retrieveAccountReclaimPayout(123);
        await comp.$nextTick();

        // THEN
        expect(comp.accountReclaimPayout).toBe(foundAccountReclaimPayout);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAccountReclaimPayout = { id: 123 };
        accountReclaimPayoutServiceStub.find.resolves(foundAccountReclaimPayout);

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
