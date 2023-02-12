/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoteManagerComponent from '@/entities/vote-manager/vote-manager.vue';
import VoteManagerClass from '@/entities/vote-manager/vote-manager.component';
import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('VoteManager Management Component', () => {
    let wrapper: Wrapper<VoteManagerClass>;
    let comp: VoteManagerClass;
    let voteManagerServiceStub: SinonStubbedInstance<VoteManagerService>;

    beforeEach(() => {
      voteManagerServiceStub = sinon.createStubInstance<VoteManagerService>(VoteManagerService);
      voteManagerServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoteManagerClass>(VoteManagerComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voteManagerService: () => voteManagerServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voteManagerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVoteManagers();
      await comp.$nextTick();

      // THEN
      expect(voteManagerServiceStub.retrieve.called).toBeTruthy();
      expect(comp.voteManagers[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voteManagerServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voteManagerServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVoteManager();
      await comp.$nextTick();

      // THEN
      expect(voteManagerServiceStub.delete.called).toBeTruthy();
      expect(voteManagerServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
