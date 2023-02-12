import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import VoterService from '@/entities/voter/voter.service';
import { IVoter } from '@/shared/model/voter.model';

import VotePayoutService from '@/entities/vote-payout/vote-payout.service';
import { IVotePayout } from '@/shared/model/vote-payout.model';

import { IVoterAccount, VoterAccount } from '@/shared/model/voter-account.model';
import VoterAccountService from './voter-account.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  voterAccount: {
    accountname: {},
    ccy: {
      required,
    },
    address: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class VoterAccountUpdate extends Vue {
  @Inject('voterAccountService') private voterAccountService: () => VoterAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public voterAccount: IVoterAccount = new VoterAccount();

  @Inject('voterService') private voterService: () => VoterService;

  public voters: IVoter[] = [];

  @Inject('votePayoutService') private votePayoutService: () => VotePayoutService;

  public votePayouts: IVotePayout[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterAccountId) {
        vm.retrieveVoterAccount(to.params.voterAccountId);
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
    if (this.voterAccount.id) {
      this.voterAccountService()
        .update(this.voterAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voterAccount.updated', { param: param.id });
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
      this.voterAccountService()
        .create(this.voterAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voterAccount.created', { param: param.id });
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

  public retrieveVoterAccount(voterAccountId): void {
    this.voterAccountService()
      .find(voterAccountId)
      .then(res => {
        this.voterAccount = res;
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
    this.votePayoutService()
      .retrieve()
      .then(res => {
        this.votePayouts = res.data;
      });
  }
}
