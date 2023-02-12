/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoteManagerPreferencesDetailComponent from '@/entities/vote-manager-preferences/vote-manager-preferences-details.vue';
import VoteManagerPreferencesClass from '@/entities/vote-manager-preferences/vote-manager-preferences-details.component';
import VoteManagerPreferencesService from '@/entities/vote-manager-preferences/vote-manager-preferences.service';
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
  describe('VoteManagerPreferences Management Detail Component', () => {
    let wrapper: Wrapper<VoteManagerPreferencesClass>;
    let comp: VoteManagerPreferencesClass;
    let voteManagerPreferencesServiceStub: SinonStubbedInstance<VoteManagerPreferencesService>;

    beforeEach(() => {
      voteManagerPreferencesServiceStub = sinon.createStubInstance<VoteManagerPreferencesService>(VoteManagerPreferencesService);

      wrapper = shallowMount<VoteManagerPreferencesClass>(VoteManagerPreferencesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voteManagerPreferencesService: () => voteManagerPreferencesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoteManagerPreferences = { id: 123 };
        voteManagerPreferencesServiceStub.find.resolves(foundVoteManagerPreferences);

        // WHEN
        comp.retrieveVoteManagerPreferences(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voteManagerPreferences).toBe(foundVoteManagerPreferences);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteManagerPreferences = { id: 123 };
        voteManagerPreferencesServiceStub.find.resolves(foundVoteManagerPreferences);

        // WHEN
        comp.beforeRouteEnter({ params: { voteManagerPreferencesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voteManagerPreferences).toBe(foundVoteManagerPreferences);
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
