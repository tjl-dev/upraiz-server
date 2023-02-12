import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IVoterPreferences } from '@/shared/model/voter-preferences.model';

import VoterPreferencesService from './voter-preferences.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class VoterPreferences extends Vue {
  @Inject('voterPreferencesService') private voterPreferencesService: () => VoterPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public voterPreferences: IVoterPreferences[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllVoterPreferencess();
  }

  public clear(): void {
    this.retrieveAllVoterPreferencess();
  }

  public retrieveAllVoterPreferencess(): void {
    this.isFetching = true;
    this.voterPreferencesService()
      .retrieve()
      .then(
        res => {
          this.voterPreferences = res.data;
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

  public prepareRemove(instance: IVoterPreferences): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeVoterPreferences(): void {
    this.voterPreferencesService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('upraizApp.voterPreferences.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllVoterPreferencess();
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
