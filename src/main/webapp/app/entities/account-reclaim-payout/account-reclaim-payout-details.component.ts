import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';
import AccountReclaimPayoutService from './account-reclaim-payout.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AccountReclaimPayoutDetails extends Vue {
  @Inject('accountReclaimPayoutService') private accountReclaimPayoutService: () => AccountReclaimPayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  public accountReclaimPayout: IAccountReclaimPayout = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.accountReclaimPayoutId) {
        vm.retrieveAccountReclaimPayout(to.params.accountReclaimPayoutId);
      }
    });
  }

  public retrieveAccountReclaimPayout(accountReclaimPayoutId) {
    this.accountReclaimPayoutService()
      .find(accountReclaimPayoutId)
      .then(res => {
        this.accountReclaimPayout = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
