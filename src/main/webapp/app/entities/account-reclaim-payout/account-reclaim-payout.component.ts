import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';

import AccountReclaimPayoutService from './account-reclaim-payout.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class AccountReclaimPayout extends Vue {
  @Inject('accountReclaimPayoutService') private accountReclaimPayoutService: () => AccountReclaimPayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public accountReclaimPayouts: IAccountReclaimPayout[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllAccountReclaimPayouts();
  }

  public clear(): void {
    this.retrieveAllAccountReclaimPayouts();
  }

  public retrieveAllAccountReclaimPayouts(): void {
    this.isFetching = true;
    this.accountReclaimPayoutService()
      .retrieve()
      .then(
        res => {
          this.accountReclaimPayouts = res.data;
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

  public prepareRemove(instance: IAccountReclaimPayout): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAccountReclaimPayout(): void {
    this.accountReclaimPayoutService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.accountReclaimPayout.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllAccountReclaimPayouts();
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
