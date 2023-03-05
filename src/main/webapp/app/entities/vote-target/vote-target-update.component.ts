import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { IVoteTarget, VoteTarget } from '@/shared/model/vote-target.model';
import VoteTargetService from './vote-target.service';
import { VoteTargetType } from '@/shared/model/enumerations/vote-target-type.model';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  voteTarget: {
    url: {
      required,
    },
    votetype: {
      required,
    },
    payout: {
      required,
      decimal,
    },
    ccy: {
      required,
    },
    comment: {},
    active: {},
    funded: {},
    created: {},
    expiry: {},
    boosted: {},
  },
};

@Component({
  validations,
})
export default class VoteTargetUpdate extends Vue {
  @Inject('voteTargetService') private voteTargetService: () => VoteTargetService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteTarget: IVoteTarget = new VoteTarget();

  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;

  public voteManagers: IVoteManager[] = [];
  public voteTargetTypeValues: string[] = Object.keys(VoteTargetType);
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteTargetId) {
        vm.retrieveVoteTarget(to.params.voteTargetId);
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
    if (this.voteTarget.id) {
      this.voteTargetService()
        .update(this.voteTarget)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteTarget.updated', { param: param.id });
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
      this.voteTargetService()
        .create(this.voteTarget)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteTarget.created', { param: param.id });
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
      this.voteTarget[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voteTarget[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.voteTarget[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voteTarget[field] = null;
    }
  }

  public retrieveVoteTarget(voteTargetId): void {
    this.voteTargetService()
      .find(voteTargetId)
      .then(res => {
        res.created = new Date(res.created);
        res.expiry = new Date(res.expiry);
        this.voteTarget = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voteManagerService()
      .retrieve()
      .then(res => {
        this.voteManagers = res.data;
      });
  }
}
