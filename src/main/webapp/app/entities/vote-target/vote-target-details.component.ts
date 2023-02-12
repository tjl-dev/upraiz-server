import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoteTarget } from '@/shared/model/vote-target.model';
import VoteTargetService from './vote-target.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoteTargetDetails extends Vue {
  @Inject('voteTargetService') private voteTargetService: () => VoteTargetService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteTarget: IVoteTarget = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteTargetId) {
        vm.retrieveVoteTarget(to.params.voteTargetId);
      }
    });
  }

  public retrieveVoteTarget(voteTargetId) {
    this.voteTargetService()
      .find(voteTargetId)
      .then(res => {
        this.voteTarget = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
