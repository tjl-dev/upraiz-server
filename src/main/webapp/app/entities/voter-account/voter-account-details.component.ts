import { Component, Vue, Inject } from 'vue-property-decorator';

import { IVoterAccount } from '@/shared/model/voter-account.model';
import VoterAccountService from './voter-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class VoterAccountDetails extends Vue {
  @Inject('voterAccountService') private voterAccountService: () => VoterAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public voterAccount: IVoterAccount = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterAccountId) {
        vm.retrieveVoterAccount(to.params.voterAccountId);
      }
    });
  }

  public retrieveVoterAccount(voterAccountId) {
    this.voterAccountService()
      .find(voterAccountId)
      .then(res => {
        this.voterAccount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
