/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoterComponent from '@/entities/voter/voter.vue';
import VoterClass from '@/entities/voter/voter.component';
import VoterService from '@/entities/voter/voter.service';
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
  describe('Voter Management Component', () => {
    let wrapper: Wrapper<VoterClass>;
    let comp: VoterClass;
    let voterServiceStub: SinonStubbedInstance<VoterService>;

    beforeEach(() => {
      voterServiceStub = sinon.createStubInstance<VoterService>(VoterService);
      voterServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoterClass>(VoterComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voterService: () => voterServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voterServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVoters();
      await comp.$nextTick();

      // THEN
      expect(voterServiceStub.retrieve.called).toBeTruthy();
      expect(comp.voters[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voterServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voterServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVoter();
      await comp.$nextTick();

      // THEN
      expect(voterServiceStub.delete.called).toBeTruthy();
      expect(voterServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
