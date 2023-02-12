import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IManagedAccount } from '@/shared/model/managed-account.model';

import ManagedAccountService from './managed-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ManagedAccount extends Vue {
  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public managedAccounts: IManagedAccount[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllManagedAccounts();
  }

  public clear(): void {
    this.retrieveAllManagedAccounts();
  }

  public retrieveAllManagedAccounts(): void {
    this.isFetching = true;
    this.managedAccountService()
      .retrieve()
      .then(
        res => {
          this.managedAccounts = res.data;
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

  public prepareRemove(instance: IManagedAccount): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeManagedAccount(): void {
    this.managedAccountService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.managedAccount.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllManagedAccounts();
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
