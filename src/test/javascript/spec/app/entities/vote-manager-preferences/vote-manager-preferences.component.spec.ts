/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoteManagerPreferencesComponent from '@/entities/vote-manager-preferences/vote-manager-preferences.vue';
import VoteManagerPreferencesClass from '@/entities/vote-manager-preferences/vote-manager-preferences.component';
import VoteManagerPreferencesService from '@/entities/vote-manager-preferences/vote-manager-preferences.service';
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
  describe('VoteManagerPreferences Management Component', () => {
    let wrapper: Wrapper<VoteManagerPreferencesClass>;
    let comp: VoteManagerPreferencesClass;
    let voteManagerPreferencesServiceStub: SinonStubbedInstance<VoteManagerPreferencesService>;

    beforeEach(() => {
      voteManagerPreferencesServiceStub = sinon.createStubInstance<VoteManagerPreferencesService>(VoteManagerPreferencesService);
      voteManagerPreferencesServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoteManagerPreferencesClass>(VoteManagerPreferencesComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voteManagerPreferencesService: () => voteManagerPreferencesServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voteManagerPreferencesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVoteManagerPreferencess();
      await comp.$nextTick();

      // THEN
      expect(voteManagerPreferencesServiceStub.retrieve.called).toBeTruthy();
      expect(comp.voteManagerPreferences[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voteManagerPreferencesServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voteManagerPreferencesServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVoteManagerPreferences();
      await comp.$nextTick();

      // THEN
      expect(voteManagerPreferencesServiceStub.delete.called).toBeTruthy();
      expect(voteManagerPreferencesServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
