/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AccountReclaimRequestDetailComponent from '@/entities/account-reclaim-request/account-reclaim-request-details.vue';
import AccountReclaimRequestClass from '@/entities/account-reclaim-request/account-reclaim-request-details.component';
import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
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
  describe('AccountReclaimRequest Management Detail Component', () => {
    let wrapper: Wrapper<AccountReclaimRequestClass>;
    let comp: AccountReclaimRequestClass;
    let accountReclaimRequestServiceStub: SinonStubbedInstance<AccountReclaimRequestService>;

    beforeEach(() => {
      accountReclaimRequestServiceStub = sinon.createStubInstance<AccountReclaimRequestService>(AccountReclaimRequestService);

      wrapper = shallowMount<AccountReclaimRequestClass>(AccountReclaimRequestDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { accountReclaimRequestService: () => accountReclaimRequestServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAccountReclaimRequest = { id: 123 };
        accountReclaimRequestServiceStub.find.resolves(foundAccountReclaimRequest);

        // WHEN
        comp.retrieveAccountReclaimRequest(123);
        await comp.$nextTick();

        // THEN
        expect(comp.accountReclaimRequest).toBe(foundAccountReclaimRequest);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAccountReclaimRequest = { id: 123 };
        accountReclaimRequestServiceStub.find.resolves(foundAccountReclaimRequest);

        // WHEN
        comp.beforeRouteEnter({ params: { accountReclaimRequestId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.accountReclaimRequest).toBe(foundAccountReclaimRequest);
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
