/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AccountReclaimPayoutComponent from '@/entities/account-reclaim-payout/account-reclaim-payout.vue';
import AccountReclaimPayoutClass from '@/entities/account-reclaim-payout/account-reclaim-payout.component';
import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';
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
  describe('AccountReclaimPayout Management Component', () => {
    let wrapper: Wrapper<AccountReclaimPayoutClass>;
    let comp: AccountReclaimPayoutClass;
    let accountReclaimPayoutServiceStub: SinonStubbedInstance<AccountReclaimPayoutService>;

    beforeEach(() => {
      accountReclaimPayoutServiceStub = sinon.createStubInstance<AccountReclaimPayoutService>(AccountReclaimPayoutService);
      accountReclaimPayoutServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AccountReclaimPayoutClass>(AccountReclaimPayoutComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          accountReclaimPayoutService: () => accountReclaimPayoutServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      accountReclaimPayoutServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAccountReclaimPayouts();
      await comp.$nextTick();

      // THEN
      expect(accountReclaimPayoutServiceStub.retrieve.called).toBeTruthy();
      expect(comp.accountReclaimPayouts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      accountReclaimPayoutServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(accountReclaimPayoutServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAccountReclaimPayout();
      await comp.$nextTick();

      // THEN
      expect(accountReclaimPayoutServiceStub.delete.called).toBeTruthy();
      expect(accountReclaimPayoutServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
