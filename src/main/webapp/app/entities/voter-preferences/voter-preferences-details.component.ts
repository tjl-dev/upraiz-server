import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoterPreferences } from '@/shared/model/voter-preferences.model';
import VoterPreferencesService from './voter-preferences.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoterPreferencesDetails extends Vue {
  @Inject('voterPreferencesService') private voterPreferencesService: () => VoterPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  public voterPreferences: IVoterPreferences = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterPreferencesId) {
        vm.retrieveVoterPreferences(to.params.voterPreferencesId);
      }
    });
  }

  public retrieveVoterPreferences(voterPreferencesId) {
    this.voterPreferencesService()
      .find(voterPreferencesId)
      .then(res => {
        this.voterPreferences = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
