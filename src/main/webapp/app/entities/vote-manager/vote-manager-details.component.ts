import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoteManager } from '@/shared/model/vote-manager.model';
import VoteManagerService from './vote-manager.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoteManagerDetails extends Vue {
  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteManager: IVoteManager = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteManagerId) {
        vm.retrieveVoteManager(to.params.voteManagerId);
      }
    });
  }

  public retrieveVoteManager(voteManagerId) {
    this.voteManagerService()
      .find(voteManagerId)
      .then(res => {
        this.voteManager = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
