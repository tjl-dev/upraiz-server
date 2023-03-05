import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import VotePayoutService from '@/entities/vote-payout/vote-payout.service';
import { IVotePayout } from '@/shared/model/vote-payout.model';

import VoteTargetService from '@/entities/vote-target/vote-target.service';
import { IVoteTarget } from '@/shared/model/vote-target.model';

import VoterService from '@/entities/voter/voter.service';
import { IVoter } from '@/shared/model/voter.model';

import { IVote, Vote } from '@/shared/model/vote.model';
import VoteService from './vote.service';

const validations: any = {
  vote: {
    votedTimestamp: {
      required,
    },
    verified: {},
    verifiedTime: {},
    verifiedBy: {},
    paid: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class VoteUpdate extends Vue {
  @Inject('voteService') private voteService: () => VoteService;
  @Inject('alertService') private alertService: () => AlertService;

  public vote: IVote = new Vote();

  @Inject('votePayoutService') private votePayoutService: () => VotePayoutService;

  public votePayouts: IVotePayout[] = [];

  @Inject('voteTargetService') private voteTargetService: () => VoteTargetService;

  public voteTargets: IVoteTarget[] = [];

  @Inject('voterService') private voterService: () => VoterService;

  public voters: IVoter[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteId) {
        vm.retrieveVote(to.params.voteId);
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
    if (this.vote.id) {
      this.voteService()
        .update(this.vote)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.vote.updated', { param: param.id });
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
      this.voteService()
        .create(this.vote)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.vote.created', { param: param.id });
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
      this.vote[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.vote[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.vote[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.vote[field] = null;
    }
  }

  public retrieveVote(voteId): void {
    this.voteService()
      .find(voteId)
      .then(res => {
        res.votedTimestamp = new Date(res.votedTimestamp);
        res.verifiedTime = new Date(res.verifiedTime);
        this.vote = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.votePayoutService()
      .retrieve()
      .then(res => {
        this.votePayouts = res.data;
      });
    this.voteTargetService()
      .retrieve()
      .then(res => {
        this.voteTargets = res.data;
      });
    this.voterService()
      .retrieve()
      .then(res => {
        this.voters = res.data;
      });
  }
}
