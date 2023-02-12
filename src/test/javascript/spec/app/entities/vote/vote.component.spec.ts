/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoteComponent from '@/entities/vote/vote.vue';
import VoteClass from '@/entities/vote/vote.component';
import VoteService from '@/entities/vote/vote.service';
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
  describe('Vote Management Component', () => {
    let wrapper: Wrapper<VoteClass>;
    let comp: VoteClass;
    let voteServiceStub: SinonStubbedInstance<VoteService>;

    beforeEach(() => {
      voteServiceStub = sinon.createStubInstance<VoteService>(VoteService);
      voteServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoteClass>(VoteComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voteService: () => voteServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voteServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVotes();
      await comp.$nextTick();

      // THEN
      expect(voteServiceStub.retrieve.called).toBeTruthy();
      expect(comp.votes[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voteServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voteServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVote();
      await comp.$nextTick();

      // THEN
      expect(voteServiceStub.delete.called).toBeTruthy();
      expect(voteServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
