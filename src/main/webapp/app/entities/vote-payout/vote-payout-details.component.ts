import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVotePayout } from '@/shared/model/vote-payout.model';
import VotePayoutService from './vote-payout.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VotePayoutDetails extends Vue {
  @Inject('votePayoutService') private votePayoutService: () => VotePayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  public votePayout: IVotePayout = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.votePayoutId) {
        vm.retrieveVotePayout(to.params.votePayoutId);
      }
    });
  }

  public retrieveVotePayout(votePayoutId) {
    this.votePayoutService()
      .find(votePayoutId)
      .then(res => {
        this.votePayout = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
