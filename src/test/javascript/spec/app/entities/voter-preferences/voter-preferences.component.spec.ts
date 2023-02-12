/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoterPreferencesComponent from '@/entities/voter-preferences/voter-preferences.vue';
import VoterPreferencesClass from '@/entities/voter-preferences/voter-preferences.component';
import VoterPreferencesService from '@/entities/voter-preferences/voter-preferences.service';
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
  describe('VoterPreferences Management Component', () => {
    let wrapper: Wrapper<VoterPreferencesClass>;
    let comp: VoterPreferencesClass;
    let voterPreferencesServiceStub: SinonStubbedInstance<VoterPreferencesService>;

    beforeEach(() => {
      voterPreferencesServiceStub = sinon.createStubInstance<VoterPreferencesService>(VoterPreferencesService);
      voterPreferencesServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoterPreferencesClass>(VoterPreferencesComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voterPreferencesService: () => voterPreferencesServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voterPreferencesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVoterPreferencess();
      await comp.$nextTick();

      // THEN
      expect(voterPreferencesServiceStub.retrieve.called).toBeTruthy();
      expect(comp.voterPreferences[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voterPreferencesServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voterPreferencesServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVoterPreferences();
      await comp.$nextTick();

      // THEN
      expect(voterPreferencesServiceStub.delete.called).toBeTruthy();
      expect(voterPreferencesServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
