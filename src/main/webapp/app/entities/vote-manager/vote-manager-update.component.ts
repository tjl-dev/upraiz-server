import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import VoteManagerPreferencesService from '@/entities/vote-manager-preferences/vote-manager-preferences.service';
import { IVoteManagerPreferences } from '@/shared/model/vote-manager-preferences.model';

import ManagedAccountService from '@/entities/managed-account/managed-account.service';
import { IManagedAccount } from '@/shared/model/managed-account.model';

import VoteTargetService from '@/entities/vote-target/vote-target.service';
import { IVoteTarget } from '@/shared/model/vote-target.model';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';

import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';
import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';

import { IVoteManager, VoteManager } from '@/shared/model/vote-manager.model';
import VoteManagerService from './vote-manager.service';

const validations: any = {
  voteManager: {
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
export default class VoteManagerUpdate extends Vue {
  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;
  @Inject('alertService') private alertService: () => AlertService;

  public voteManager: IVoteManager = new VoteManager();

  @Inject('voteManagerPreferencesService') private voteManagerPreferencesService: () => VoteManagerPreferencesService;

  public voteManagerPreferences: IVoteManagerPreferences[] = [];

  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;

  public managedAccounts: IManagedAccount[] = [];

  @Inject('voteTargetService') private voteTargetService: () => VoteTargetService;

  public voteTargets: IVoteTarget[] = [];

  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;

  public accountReclaimRequests: IAccountReclaimRequest[] = [];

  @Inject('accountReclaimPayoutService') private accountReclaimPayoutService: () => AccountReclaimPayoutService;

  public accountReclaimPayouts: IAccountReclaimPayout[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.voteManagerId) {
        vm.retrieveVoteManager(to.params.voteManagerId);
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
    if (this.voteManager.id) {
      this.voteManagerService()
        .update(this.voteManager)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteManager.updated', { param: param.id });
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
      this.voteManagerService()
        .create(this.voteManager)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.voteManager.created', { param: param.id });
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
      this.voteManager[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voteManager[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.voteManager[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.voteManager[field] = null;
    }
  }

  public retrieveVoteManager(voteManagerId): void {
    this.voteManagerService()
      .find(voteManagerId)
      .then(res => {
        res.created = new Date(res.created);
        this.voteManager = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.voteManagerPreferencesService()
      .retrieve()
      .then(res => {
        this.voteManagerPreferences = res.data;
      });
    this.managedAccountService()
      .retrieve()
      .then(res => {
        this.managedAccounts = res.data;
      });
    this.voteTargetService()
      .retrieve()
      .then(res => {
        this.voteTargets = res.data;
      });
    this.accountReclaimRequestService()
      .retrieve()
      .then(res => {
        this.accountReclaimRequests = res.data;
      });
    this.accountReclaimPayoutService()
      .retrieve()
      .then(res => {
        this.accountReclaimPayouts = res.data;
      });
  }
}
