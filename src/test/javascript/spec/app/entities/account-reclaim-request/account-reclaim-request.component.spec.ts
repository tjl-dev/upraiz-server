/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AccountReclaimRequestComponent from '@/entities/account-reclaim-request/account-reclaim-request.vue';
import AccountReclaimRequestClass from '@/entities/account-reclaim-request/account-reclaim-request.component';
import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
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
  describe('AccountReclaimRequest Management Component', () => {
    let wrapper: Wrapper<AccountReclaimRequestClass>;
    let comp: AccountReclaimRequestClass;
    let accountReclaimRequestServiceStub: SinonStubbedInstance<AccountReclaimRequestService>;

    beforeEach(() => {
      accountReclaimRequestServiceStub = sinon.createStubInstance<AccountReclaimRequestService>(AccountReclaimRequestService);
      accountReclaimRequestServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AccountReclaimRequestClass>(AccountReclaimRequestComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          accountReclaimRequestService: () => accountReclaimRequestServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      accountReclaimRequestServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAccountReclaimRequests();
      await comp.$nextTick();

      // THEN
      expect(accountReclaimRequestServiceStub.retrieve.called).toBeTruthy();
      expect(comp.accountReclaimRequests[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      accountReclaimRequestServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(accountReclaimRequestServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAccountReclaimRequest();
      await comp.$nextTick();

      // THEN
      expect(accountReclaimRequestServiceStub.delete.called).toBeTruthy();
      expect(accountReclaimRequestServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
