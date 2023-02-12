import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import VoteManagerService from './vote-manager.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VoteManager extends Vue {
  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voteManagers: IVoteManager[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoteManagers();
  }

  public clear(): void {
    this.retrieveAllVoteManagers();
  }

  public retrieveAllVoteManagers(): void {
    this.isFetching = true;
    this.voteManagerService()
      .retrieve()
      .then(
        res => {
          this.voteManagers = res.data;
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

  public prepareRemove(instance: IVoteManager): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoteManager(): void {
    this.voteManagerService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voteManager.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoteManagers();
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
