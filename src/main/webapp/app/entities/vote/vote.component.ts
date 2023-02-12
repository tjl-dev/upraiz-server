import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVote } from '@/shared/model/vote.model';

import VoteService from './vote.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Vote extends Vue {
  @Inject('voteService') private voteService: () => VoteService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public votes: IVote[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVotes();
  }

  public clear(): void {
    this.retrieveAllVotes();
  }

  public retrieveAllVotes(): void {
    this.isFetching = true;
    this.voteService()
      .retrieve()
      .then(
        res => {
          this.votes = res.data;
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

  public prepareRemove(instance: IVote): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVote(): void {
    this.voteService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.vote.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVotes();
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
