import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoterAccount } from '@/shared/model/voter-account.model';

import VoterAccountService from './voter-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VoterAccount extends Vue {
  @Inject('voterAccountService') private voterAccountService: () => VoterAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voterAccounts: IVoterAccount[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoterAccounts();
  }

  public clear(): void {
    this.retrieveAllVoterAccounts();
  }

  public retrieveAllVoterAccounts(): void {
    this.isFetching = true;
    this.voterAccountService()
      .retrieve()
      .then(
        res => {
          this.voterAccounts = res.data;
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

  public prepareRemove(instance: IVoterAccount): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoterAccount(): void {
    this.voterAccountService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voterAccount.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoterAccounts();
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
