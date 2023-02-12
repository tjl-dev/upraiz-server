import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { IVoteManagerPreferences, VoteManagerPreferences } from '@/shared/model/vote-manager-preferences.model';
import VoteManagerPreferencesService from './vote-manager-preferences.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  voteManagerPreferences: {
    payCcy: {},
  },
};

@Component({
  validations,
})
export default class VoteManagerPreferencesUpdate extends Vue {
  @Inject('voteManagerPreferencesService') private voteManagerPreferencesService: () => VoteManagerPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteManagerPreferences: IVoteManagerPreferences = new VoteManagerPreferences();

  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;

  public voteManagers: IVoteManager[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteManagerPreferencesId) {
        vm.retrieveVoteManagerPreferences(to.params.voteManagerPreferencesId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.voteManagerPreferences.id) {
      this.voteManagerPreferencesService()
        .update(this.voteManagerPreferences)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteManagerPreferences.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.voteManagerPreferencesService()
        .create(this.voteManagerPreferences)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteManagerPreferences.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveVoteManagerPreferences(voteManagerPreferencesId): void {
    this.voteManagerPreferencesService()
      .find(voteManagerPreferencesId)
      .then(res => {
        this.voteManagerPreferences = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voteManagerService()
      .retrieve()
      .then(res => {
        this.voteManagers = res.data;
      });
  }
}
