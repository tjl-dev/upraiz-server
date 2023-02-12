/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoterDetailComponent from '@/entities/voter/voter-details.vue';
import VoterClass from '@/entities/voter/voter-details.component';
import VoterService from '@/entities/voter/voter.service';
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
  describe('Voter Management Detail Component', () => {
    let wrapper: Wrapper<VoterClass>;
    let comp: VoterClass;
    let voterServiceStub: SinonStubbedInstance<VoterService>;

    beforeEach(() => {
      voterServiceStub = sinon.createStubInstance<VoterService>(VoterService);

      wrapper = shallowMount<VoterClass>(VoterDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voterService: () => voterServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoter = { id: 123 };
        voterServiceStub.find.resolves(foundVoter);

        // WHEN
        comp.retrieveVoter(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voter).toBe(foundVoter);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoter = { id: 123 };
        voterServiceStub.find.resolves(foundVoter);

        // WHEN
        comp.beforeRouteEnter({ params: { voterId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voter).toBe(foundVoter);
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
