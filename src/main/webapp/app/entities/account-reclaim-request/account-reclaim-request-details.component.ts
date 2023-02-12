import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';
import AccountReclaimRequestService from './account-reclaim-request.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AccountReclaimRequestDetails extends Vue {
  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;
  @Inject('alertService') private alertService: () => AlertService;

  public accountReclaimRequest: IAccountReclaimRequest = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.accountReclaimRequestId) {
        vm.retrieveAccountReclaimRequest(to.params.accountReclaimRequestId);
      }
    });
  }

  public retrieveAccountReclaimRequest(accountReclaimRequestId) {
    this.accountReclaimRequestService()
      .find(accountReclaimRequestId)
      .then(res => {
        this.accountReclaimRequest = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
