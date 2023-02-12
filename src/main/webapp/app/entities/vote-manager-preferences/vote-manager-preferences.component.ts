import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoteManagerPreferences } from '@/shared/model/vote-manager-preferences.model';

import VoteManagerPreferencesService from './vote-manager-preferences.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VoteManagerPreferences extends Vue {
  @Inject('voteManagerPreferencesService') private voteManagerPreferencesService: () => VoteManagerPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voteManagerPreferences: IVoteManagerPreferences[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoteManagerPreferencess();
  }

  public clear(): void {
    this.retrieveAllVoteManagerPreferencess();
  }

  public retrieveAllVoteManagerPreferencess(): void {
    this.isFetching = true;
    this.voteManagerPreferencesService()
      .retrieve()
      .then(
        res => {
          this.voteManagerPreferences = res.data;
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

  public prepareRemove(instance: IVoteManagerPreferences): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoteManagerPreferences(): void {
    this.voteManagerPreferencesService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voteManagerPreferences.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoteManagerPreferencess();
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
