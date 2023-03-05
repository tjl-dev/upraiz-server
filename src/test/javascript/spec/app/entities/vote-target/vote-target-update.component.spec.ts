/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import VoteTargetUpdateComponent from '@/entities/vote-target/vote-target-update.vue';
import VoteTargetClass from '@/entities/vote-target/vote-target-update.component';
import VoteTargetService from '@/entities/vote-target/vote-target.service';

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
  describe('VoteTarget Management Update Component', () => {
    let wrapper: Wrapper<VoteTargetClass>;
    let comp: VoteTargetClass;
    let voteTargetServiceStub: SinonStubbedInstance<VoteTargetService>;

    beforeEach(() => {
      voteTargetServiceStub = sinon.createStubInstance<VoteTargetService>(VoteTargetService);

      wrapper = shallowMount<VoteTargetClass>(VoteTargetUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          voteTargetService: () => voteTargetServiceStub,
          alertService: () => new AlertService(),

          voteManagerService: () =>
            sinon.createStubInstance<VoteManagerService>(VoteManagerService, {
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
        comp.voteTarget = entity;
        voteTargetServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteTargetServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.voteTarget = entity;
        voteTargetServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(voteTargetServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundVoteTarget = { id: 123 };
        voteTargetServiceStub.find.resolves(foundVoteTarget);
        voteTargetServiceStub.retrieve.resolves([foundVoteTarget]);

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
