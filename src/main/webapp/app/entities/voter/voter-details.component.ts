import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoter } from '@/shared/model/voter.model';
import VoterService from './voter.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoterDetails extends Vue {
  @Inject('voterService') private voterService: () => VoterService;
  @Inject('alertService') private alertService: () => AlertService;

  public voter: IVoter = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterId) {
        vm.retrieveVoter(to.params.voterId);
      }
    });
  }

  public retrieveVoter(voterId) {
    this.voterService()
      .find(voterId)
      .then(res => {
        this.voter = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
