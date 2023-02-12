/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import VoterPreferencesDetailComponent from '@/entities/voter-preferences/voter-preferences-details.vue';
import VoterPreferencesClass from '@/entities/voter-preferences/voter-preferences-details.component';
import VoterPreferencesService from '@/entities/voter-preferences/voter-preferences.service';
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
  describe('VoterPreferences Management Detail Component', () => {
    let wrapper: Wrapper<VoterPreferencesClass>;
    let comp: VoterPreferencesClass;
    let voterPreferencesServiceStub: SinonStubbedInstance<VoterPreferencesService>;

    beforeEach(() => {
      voterPreferencesServiceStub = sinon.createStubInstance<VoterPreferencesService>(VoterPreferencesService);

      wrapper = shallowMount<VoterPreferencesClass>(VoterPreferencesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { voterPreferencesService: () => voterPreferencesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundVoterPreferences = { id: 123 };
        voterPreferencesServiceStub.find.resolves(foundVoterPreferences);

        // WHEN
        comp.retrieveVoterPreferences(123);
        await comp.$nextTick();

        // THEN
        expect(comp.voterPreferences).toBe(foundVoterPreferences);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoterPreferences = { id: 123 };
        voterPreferencesServiceStub.find.resolves(foundVoterPreferences);

        // WHEN
        comp.beforeRouteEnter({ params: { voterPreferencesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.voterPreferences).toBe(foundVoterPreferences);
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
