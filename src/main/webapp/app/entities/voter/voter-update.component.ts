import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import VoterPreferencesService from '@/entities/voter-preferences/voter-preferences.service';
import { IVoterPreferences } from '@/shared/model/voter-preferences.model';

import VoterAccountService from '@/entities/voter-account/voter-account.service';
import { IVoterAccount } from '@/shared/model/voter-account.model';

import VoteService from '@/entities/vote/vote.service';
import { IVote } from '@/shared/model/vote.model';

import { IVoter, Voter } from '@/shared/model/voter.model';
import VoterService from './voter.service';

const validations: any = {
  voter: {
    email: {
      required,
    },
    name: {
      required,
    },
    created: {},
    active: {},
    comment: {},
  },
};

@Component({
  validations,
})
export default class VoterUpdate extends Vue {
  @Inject('voterService') private voterService: () => VoterService;
  @Inject('alertService') private alertService: () => AlertService;

  public voter: IVoter = new Voter();

  @Inject('voterPreferencesService') private voterPreferencesService: () => VoterPreferencesService;

  public voterPreferences: IVoterPreferences[] = [];

  @Inject('voterAccountService') private voterAccountService: () => VoterAccountService;

  public voterAccounts: IVoterAccount[] = [];

  @Inject('voteService') private voteService: () => VoteService;

  public votes: IVote[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voterId) {
        vm.retrieveVoter(to.params.voterId);
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
    if (this.voter.id) {
      this.voterService()
        .update(this.voter)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voter.updated', { param: param.id });
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
      this.voterService()
        .create(this.voter)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voter.created', { param: param.id });
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
      this.voter[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voter[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.voter[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voter[field] = null;
    }
  }

  public retrieveVoter(voterId): void {
    this.voterService()
      .find(voterId)
      .then(res => {
        res.created = new Date(res.created);
        this.voter = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voterPreferencesService()
      .retrieve()
      .then(res => {
        this.voterPreferences = res.data;
      });
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
