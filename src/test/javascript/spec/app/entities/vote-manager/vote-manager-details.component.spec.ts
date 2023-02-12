/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoteManagerDetailComponent from '@/entities/vote-manager/vote-manager-details.vue';
import VoteManagerClass from '@/entities/vote-manager/vote-manager-details.component';
import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
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
  describe('VoteManager Management Detail Component', () => {
    let wrapper: Wrapper<VoteManagerClass>;
    let comp: VoteManagerClass;
    let voteManagerServiceStub: SinonStubbedInstance<VoteManagerService>;

    beforeEach(() => {
      voteManagerServiceStub = sinon.createStubInstance<VoteManagerService>(VoteManagerService);

      wrapper = shallowMount<VoteManagerClass>(VoteManagerDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voteManagerService: () => voteManagerServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoteManager = { id: 123 };
        voteManagerServiceStub.find.resolves(foundVoteManager);

        // WHEN
        comp.retrieveVoteManager(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voteManager).toBe(foundVoteManager);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteManager = { id: 123 };
        voteManagerServiceStub.find.resolves(foundVoteManager);

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
