import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoteManagerPreferences } from '@/shared/model/vote-manager-preferences.model';
import VoteManagerPreferencesService from './vote-manager-preferences.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoteManagerPreferencesDetails extends Vue {
  @Inject('voteManagerPreferencesService') private voteManagerPreferencesService: () => VoteManagerPreferencesService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteManagerPreferences: IVoteManagerPreferences = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteManagerPreferencesId) {
        vm.retrieveVoteManagerPreferences(to.params.voteManagerPreferencesId);
      }
    });
  }

  public retrieveVoteManagerPreferences(voteManagerPreferencesId) {
    this.voteManagerPreferencesService()
      .find(voteManagerPreferencesId)
      .then(res => {
        this.voteManagerPreferences = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
