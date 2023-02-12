import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoter } from '@/shared/model/voter.model';

import VoterService from './voter.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Voter extends Vue {
  @Inject('voterService') private voterService: () => VoterService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voters: IVoter[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoters();
  }

  public clear(): void {
    this.retrieveAllVoters();
  }

  public retrieveAllVoters(): void {
    this.isFetching = true;
    this.voterService()
      .retrieve()
      .then(
        res => {
          this.voters = res.data;
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

  public prepareRemove(instance: IVoter): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoter(): void {
    this.voterService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voter.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoters();
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
