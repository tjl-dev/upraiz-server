/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VotePayoutComponent from '@/entities/vote-payout/vote-payout.vue';
import VotePayoutClass from '@/entities/vote-payout/vote-payout.component';
import VotePayoutService from '@/entities/vote-payout/vote-payout.service';
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
  describe('VotePayout Management Component', () => {
    let wrapper: Wrapper<VotePayoutClass>;
    let comp: VotePayoutClass;
    let votePayoutServiceStub: SinonStubbedInstance<VotePayoutService>;

    beforeEach(() => {
      votePayoutServiceStub = sinon.createStubInstance<VotePayoutService>(VotePayoutService);
      votePayoutServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VotePayoutClass>(VotePayoutComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          votePayoutService: () => votePayoutServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      votePayoutServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVotePayouts();
      await comp.$nextTick();

      // THEN
      expect(votePayoutServiceStub.retrieve.called).toBeTruthy();
      expect(comp.votePayouts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      votePayoutServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(votePayoutServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVotePayout();
      await comp.$nextTick();

      // THEN
      expect(votePayoutServiceStub.delete.called).toBeTruthy();
      expect(votePayoutServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
