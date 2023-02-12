/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VotePayoutDetailComponent from '@/entities/vote-payout/vote-payout-details.vue';
import VotePayoutClass from '@/entities/vote-payout/vote-payout-details.component';
import VotePayoutService from '@/entities/vote-payout/vote-payout.service';
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
  describe('VotePayout Management Detail Component', () => {
    let wrapper: Wrapper<VotePayoutClass>;
    let comp: VotePayoutClass;
    let votePayoutServiceStub: SinonStubbedInstance<VotePayoutService>;

    beforeEach(() => {
      votePayoutServiceStub = sinon.createStubInstance<VotePayoutService>(VotePayoutService);

      wrapper = shallowMount<VotePayoutClass>(VotePayoutDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { votePayoutService: () => votePayoutServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVotePayout = { id: 123 };
        votePayoutServiceStub.find.resolves(foundVotePayout);

        // WHEN
        comp.retrieveVotePayout(123);
        await comp.$nextTick();

        // THEN
        expect(comp.votePayout).toBe(foundVotePayout);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVotePayout = { id: 123 };
        votePayoutServiceStub.find.resolves(foundVotePayout);

        // WHEN
        comp.beforeRouteEnter({ params: { votePayoutId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.votePayout).toBe(foundVotePayout);
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
