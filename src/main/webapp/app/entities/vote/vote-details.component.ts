import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVote } from '@/shared/model/vote.model';
import VoteService from './vote.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoteDetails extends Vue {
  @Inject('voteService') private voteService: () => VoteService;
  @Inject('alertService') private alertService: () => AlertService;

  public vote: IVote = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteId) {
        vm.retrieveVote(to.params.voteId);
      }
    });
  }

  public retrieveVote(voteId) {
    this.voteService()
      .find(voteId)
      .then(res => {
        this.vote = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
