import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVotePayout } from '@/shared/model/vote-payout.model';

import VotePayoutService from './vote-payout.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VotePayout extends Vue {
  @Inject('votePayoutService') private votePayoutService: () => VotePayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public votePayouts: IVotePayout[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVotePayouts();
  }

  public clear(): void {
    this.retrieveAllVotePayouts();
  }

  public retrieveAllVotePayouts(): void {
    this.isFetching = true;
    this.votePayoutService()
      .retrieve()
      .then(
        res => {
          this.votePayouts = res.data;
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

  public prepareRemove(instance: IVotePayout): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVotePayout(): void {
    this.votePayoutService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.votePayout.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVotePayouts();
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
