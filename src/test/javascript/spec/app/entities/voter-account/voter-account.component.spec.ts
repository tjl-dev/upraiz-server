/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import VoterAccountComponent from '@/entities/voter-account/voter-account.vue';
import VoterAccountClass from '@/entities/voter-account/voter-account.component';
import VoterAccountService from '@/entities/voter-account/voter-account.service';
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
  describe('VoterAccount Management Component', () => {
    let wrapper: Wrapper<VoterAccountClass>;
    let comp: VoterAccountClass;
    let voterAccountServiceStub: SinonStubbedInstance<VoterAccountService>;

    beforeEach(() => {
      voterAccountServiceStub = sinon.createStubInstance<VoterAccountService>(VoterAccountService);
      voterAccountServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<VoterAccountClass>(VoterAccountComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          voterAccountService: () => voterAccountServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      voterAccountServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllVoterAccounts();
      await comp.$nextTick();

      // THEN
      expect(voterAccountServiceStub.retrieve.called).toBeTruthy();
      expect(comp.voterAccounts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      voterAccountServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(voterAccountServiceStub.retrieve.callCount).toEqual(1);

      comp.removeVoterAccount();
      await comp.$nextTick();

      // THEN
      expect(voterAccountServiceStub.delete.called).toBeTruthy();
      expect(voterAccountServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
