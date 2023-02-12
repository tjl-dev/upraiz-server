/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoterPreferencesUpdateComponent from '@/entities/voter-preferences/voter-preferences-update.vue';
import VoterPreferencesClass from '@/entities/voter-preferences/voter-preferences-update.component';
import VoterPreferencesService from '@/entities/voter-preferences/voter-preferences.service';

import VoterService from '@/entities/voter/voter.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('VoterPreferences Management Update Component', () => {
    let wrapper: Wrapper<VoterPreferencesClass>;
    let comp: VoterPreferencesClass;
    let voterPreferencesServiceStub: SinonStubbedInstance<VoterPreferencesService>;

    beforeEach(() => {
      voterPreferencesServiceStub = sinon.createStubInstance<VoterPreferencesService>(VoterPreferencesService);

      wrapper = shallowMount<VoterPreferencesClass>(VoterPreferencesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voterPreferencesService: () => voterPreferencesServiceStub,
          alertService: () => new AlertService(),

          voterService: () =>
            sinon.createStubInstance<VoterService>(VoterService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.voterPreferences = entity;
        voterPreferencesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voterPreferencesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.voterPreferences = entity;
        voterPreferencesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voterPreferencesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoterPreferences = { id: 123 };
        voterPreferencesServiceStub.find.resolves(foundVoterPreferences);
        voterPreferencesServiceStub.retrieve.resolves([foundVoterPreferences]);

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
