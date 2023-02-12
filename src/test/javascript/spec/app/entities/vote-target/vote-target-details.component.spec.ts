/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoteTargetDetailComponent from '@/entities/vote-target/vote-target-details.vue';
import VoteTargetClass from '@/entities/vote-target/vote-target-details.component';
import VoteTargetService from '@/entities/vote-target/vote-target.service';
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
  describe('VoteTarget Management Detail Component', () => {
    let wrapper: Wrapper<VoteTargetClass>;
    let comp: VoteTargetClass;
    let voteTargetServiceStub: SinonStubbedInstance<VoteTargetService>;

    beforeEach(() => {
      voteTargetServiceStub = sinon.createStubInstance<VoteTargetService>(VoteTargetService);

      wrapper = shallowMount<VoteTargetClass>(VoteTargetDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voteTargetService: () => voteTargetServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoteTarget = { id: 123 };
        voteTargetServiceStub.find.resolves(foundVoteTarget);

        // WHEN
        comp.retrieveVoteTarget(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voteTarget).toBe(foundVoteTarget);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteTarget = { id: 123 };
        voteTargetServiceStub.find.resolves(foundVoteTarget);

        // WHEN
        comp.beforeRouteEnter({ params: { voteTargetId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voteTarget).toBe(foundVoteTarget);
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
