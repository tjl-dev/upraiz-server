/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoterAccountDetailComponent from '@/entities/voter-account/voter-account-details.vue';
import VoterAccountClass from '@/entities/voter-account/voter-account-details.component';
import VoterAccountService from '@/entities/voter-account/voter-account.service';
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
  describe('VoterAccount Management Detail Component', () => {
    let wrapper: Wrapper<VoterAccountClass>;
    let comp: VoterAccountClass;
    let voterAccountServiceStub: SinonStubbedInstance<VoterAccountService>;

    beforeEach(() => {
      voterAccountServiceStub = sinon.createStubInstance<VoterAccountService>(VoterAccountService);

      wrapper = shallowMount<VoterAccountClass>(VoterAccountDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voterAccountService: () => voterAccountServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoterAccount = { id: 123 };
        voterAccountServiceStub.find.resolves(foundVoterAccount);

        // WHEN
        comp.retrieveVoterAccount(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voterAccount).toBe(foundVoterAccount);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoterAccount = { id: 123 };
        voterAccountServiceStub.find.resolves(foundVoterAccount);

        // WHEN
        comp.beforeRouteEnter({ params: { voterAccountId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voterAccount).toBe(foundVoterAccount);
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
