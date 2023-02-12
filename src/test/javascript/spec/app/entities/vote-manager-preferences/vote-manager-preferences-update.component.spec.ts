/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoteManagerPreferencesUpdateComponent from '@/entities/vote-manager-preferences/vote-manager-preferences-update.vue';
import VoteManagerPreferencesClass from '@/entities/vote-manager-preferences/vote-manager-preferences-update.component';
import VoteManagerPreferencesService from '@/entities/vote-manager-preferences/vote-manager-preferences.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
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
  describe('VoteManagerPreferences Management Update Component', () => {
    let wrapper: Wrapper<VoteManagerPreferencesClass>;
    let comp: VoteManagerPreferencesClass;
    let voteManagerPreferencesServiceStub: SinonStubbedInstance<VoteManagerPreferencesService>;

    beforeEach(() => {
      voteManagerPreferencesServiceStub = sinon.createStubInstance<VoteManagerPreferencesService>(VoteManagerPreferencesService);

      wrapper = shallowMount<VoteManagerPreferencesClass>(VoteManagerPreferencesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voteManagerPreferencesService: () => voteManagerPreferencesServiceStub,
          alertService: () => new AlertService(),

          voteManagerService: () =>
            sinon.createStubInstance<VoteManagerService>(VoteManagerService, {
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
        comp.voteManagerPreferences = entity;
        voteManagerPreferencesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteManagerPreferencesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.voteManagerPreferences = entity;
        voteManagerPreferencesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteManagerPreferencesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteManagerPreferences = { id: 123 };
        voteManagerPreferencesServiceStub.find.resolves(foundVoteManagerPreferences);
        voteManagerPreferencesServiceStub.retrieve.resolves([foundVoteManagerPreferences]);

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
