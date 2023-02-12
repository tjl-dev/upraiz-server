import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import VoterAccountService from '@/entities/voter-account/voter-account.service';
import { IVoterAccount } from '@/shared/model/voter-account.model';

import VoteService from '@/entities/vote/vote.service';
import { IVote } from '@/shared/model/vote.model';

import { IVotePayout, VotePayout } from '@/shared/model/vote-payout.model';
import VotePayoutService from './vote-payout.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  votePayout: {
    paidTime: {
      required,
    },
    paidAmount: {
      required,
      decimal,
    },
    paidCcy: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class VotePayoutUpdate extends Vue {
  @Inject('votePayoutService') private votePayoutService: () => VotePayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  public votePayout: IVotePayout = new VotePayout();

  @Inject('voterAccountService') private voterAccountService: () => VoterAccountService;

  public voterAccounts: IVoterAccount[] = [];

  @Inject('voteService') private voteService: () => VoteService;

  public votes: IVote[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.votePayoutId) {
        vm.retrieveVotePayout(to.params.votePayoutId);
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
    if (this.votePayout.id) {
      this.votePayoutService()
        .update(this.votePayout)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.votePayout.updated', { param: param.id });
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
      this.votePayoutService()
        .create(this.votePayout)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.votePayout.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.votePayout[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.votePayout[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.votePayout[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.votePayout[field] = null;
    }
  }

  public retrieveVotePayout(votePayoutId): void {
    this.votePayoutService()
      .find(votePayoutId)
      .then(res => {
        res.paidTime = new Date(res.paidTime);
        this.votePayout = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voterAccountService()
      .retrieve()
      .then(res => {
        this.voterAccounts = res.data;
      });
    this.voteService()
      .retrieve()
      .then(res => {
        this.votes = res.data;
      });
  }
}
