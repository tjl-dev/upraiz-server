/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import VoterUpdateComponent from '@/entities/voter/voter-update.vue';
import VoterClass from '@/entities/voter/voter-update.component';
import VoterService from '@/entities/voter/voter.service';

import VoterPreferencesService from '@/entities/voter-preferences/voter-preferences.service';

import VoterAccountService from '@/entities/voter-account/voter-account.service';

import VoteService from '@/entities/vote/vote.service';
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
  describe('Voter Management Update Component', () => {
    let wrapper: Wrapper<VoterClass>;
    let comp: VoterClass;
    let voterServiceStub: SinonStubbedInstance<VoterService>;

    beforeEach(() => {
      voterServiceStub = sinon.createStubInstance<VoterService>(VoterService);

      wrapper = shallowMount<VoterClass>(VoterUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voterService: () => voterServiceStub,
          alertService: () => new AlertService(),

          voterPreferencesService: () =>
            sinon.createStubInstance<VoterPreferencesService>(VoterPreferencesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voterAccountService: () =>
            sinon.createStubInstance<VoterAccountService>(VoterAccountService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          voteService: () =>
            sinon.createStubInstance<VoteService>(VoteService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.voter = entity;
        voterServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voterServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.voter = entity;
        voterServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voterServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoter = { id: 123 };
        voterServiceStub.find.resolves(foundVoter);
        voterServiceStub.retrieve.resolves([foundVoter]);

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
