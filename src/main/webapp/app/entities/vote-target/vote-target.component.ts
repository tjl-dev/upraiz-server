import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoteTarget } from '@/shared/model/vote-target.model';

import VoteTargetService from './vote-target.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VoteTarget extends Vue {
  @Inject('voteTargetService') private voteTargetService: () => VoteTargetService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voteTargets: IVoteTarget[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoteTargets();
  }

  public clear(): void {
    this.retrieveAllVoteTargets();
  }

  public retrieveAllVoteTargets(): void {
    this.isFetching = true;
    this.voteTargetService()
      .retrieve()
      .then(
        res => {
          this.voteTargets = res.data;
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

  public prepareRemove(instance: IVoteTarget): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoteTarget(): void {
    this.voteTargetService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voteTarget.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoteTargets();
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
