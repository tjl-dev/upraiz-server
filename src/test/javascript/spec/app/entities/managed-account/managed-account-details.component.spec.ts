/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ManagedAccountDetailComponent from '@/entities/managed-account/managed-account-details.vue';
import ManagedAccountClass from '@/entities/managed-account/managed-account-details.component';
import ManagedAccountService from '@/entities/managed-account/managed-account.service';
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
  describe('ManagedAccount Management Detail Component', () => {
    let wrapper: Wrapper<ManagedAccountClass>;
    let comp: ManagedAccountClass;
    let managedAccountServiceStub: SinonStubbedInstance<ManagedAccountService>;

    beforeEach(() => {
      managedAccountServiceStub = sinon.createStubInstance<ManagedAccountService>(ManagedAccountService);

      wrapper = shallowMount<ManagedAccountClass>(ManagedAccountDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { managedAccountService: () => managedAccountServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundManagedAccount = { id: 123 };
        managedAccountServiceStub.find.resolves(foundManagedAccount);

        // WHEN
        comp.retrieveManagedAccount(123);
        await comp.$nextTick();

        // THEN
        expect(comp.managedAccount).toBe(foundManagedAccount);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundManagedAccount = { id: 123 };
        managedAccountServiceStub.find.resolves(foundManagedAccount);

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
