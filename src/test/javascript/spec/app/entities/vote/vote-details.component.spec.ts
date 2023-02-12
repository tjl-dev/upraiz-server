/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoteDetailComponent from '@/entities/vote/vote-details.vue';
import VoteClass from '@/entities/vote/vote-details.component';
import VoteService from '@/entities/vote/vote.service';
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
  describe('Vote Management Detail Component', () => {
    let wrapper: Wrapper<VoteClass>;
    let comp: VoteClass;
    let voteServiceStub: SinonStubbedInstance<VoteService>;

    beforeEach(() => {
      voteServiceStub = sinon.createStubInstance<VoteService>(VoteService);

      wrapper = shallowMount<VoteClass>(VoteDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voteService: () => voteServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVote = { id: 123 };
        voteServiceStub.find.resolves(foundVote);

        // WHEN
        comp.retrieveVote(123);
        await comp.$nextTick();

        // THEN
        expect(comp.vote).toBe(foundVote);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVote = { id: 123 };
        voteServiceStub.find.resolves(foundVote);

        // WHEN
        comp.beforeRouteEnter({ params: { voteId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.vote).toBe(foundVote);
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
