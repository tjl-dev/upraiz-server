/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ManagedAccountComponent from '@/entities/managed-account/managed-account.vue';
import ManagedAccountClass from '@/entities/managed-account/managed-account.component';
import ManagedAccountService from '@/entities/managed-account/managed-account.service';
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
  describe('ManagedAccount Management Component', () => {
    let wrapper: Wrapper<ManagedAccountClass>;
    let comp: ManagedAccountClass;
    let managedAccountServiceStub: SinonStubbedInstance<ManagedAccountService>;

    beforeEach(() => {
      managedAccountServiceStub = sinon.createStubInstance<ManagedAccountService>(ManagedAccountService);
      managedAccountServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ManagedAccountClass>(ManagedAccountComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          managedAccountService: () => managedAccountServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      managedAccountServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllManagedAccounts();
      await comp.$nextTick();

      // THEN
      expect(managedAccountServiceStub.retrieve.called).toBeTruthy();
      expect(comp.managedAccounts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      managedAccountServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(managedAccountServiceStub.retrieve.callCount).toEqual(1);

      comp.removeManagedAccount();
      await comp.$nextTick();

      // THEN
      expect(managedAccountServiceStub.delete.called).toBeTruthy();
      expect(managedAccountServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
