import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import VoterService from '@/entities/voter/voter.service';
import { IVoter } from '@/shared/model/voter.model';

import { IVoterPreferences, VoterPreferences } from '@/shared/model/voter-preferences.model';
import VoterPreferencesService from './voter-preferences.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  voterPreferences: {
    receiveCcy: {},
  },
};

@Component({
  validations,
})
export default class VoterPreferencesUpdate extends Vue {
  @Inject('voterPreferencesService') private voterPreferencesService: () => VoterPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  public voterPreferences: IVoterPreferences = new VoterPreferences();

  @Inject('voterService') private voterService: () => VoterService;

  public voters: IVoter[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterPreferencesId) {
        vm.retrieveVoterPreferences(to.params.voterPreferencesId);
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
    if (this.voterPreferences.id) {
      this.voterPreferencesService()
        .update(this.voterPreferences)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voterPreferences.updated', { param: param.id });
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
      this.voterPreferencesService()
        .create(this.voterPreferences)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voterPreferences.created', { param: param.id });
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

  public retrieveVoterPreferences(voterPreferencesId): void {
    this.voterPreferencesService()
      .find(voterPreferencesId)
      .then(res => {
        this.voterPreferences = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voterService()
      .retrieve()
      .then(res => {
        this.voters = res.data;
      });
  }
}
