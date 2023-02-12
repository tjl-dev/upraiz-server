import { Component, Vue, Inject } from 'vue-property-decorator';

import { IManagedAccount } from '@/shared/model/managed-account.model';
import ManagedAccountService from './managed-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ManagedAccountDetails extends Vue {
  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public managedAccount: IManagedAccount = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.managedAccountId) {
        vm.retrieveManagedAccount(to.params.managedAccountId);
      }
    });
  }

  public retrieveManagedAccount(managedAccountId) {
    this.managedAccountService()
      .find(managedAccountId)
      .then(res => {
        this.managedAccount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
