import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';

import AccountReclaimRequestService from './account-reclaim-request.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class AccountReclaimRequest extends Vue {
  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public accountReclaimRequests: IAccountReclaimRequest[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllAccountReclaimRequests();
  }

  public clear(): void {
    this.retrieveAllAccountReclaimRequests();
  }

  public retrieveAllAccountReclaimRequests(): void {
    this.isFetching = true;
    this.accountReclaimRequestService()
      .retrieve()
      .then(
        res => {
          this.accountReclaimRequests = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IAccountReclaimRequest): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAccountReclaimRequest(): void {
    this.accountReclaimRequestService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.accountReclaimRequest.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllAccountReclaimRequests();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
