import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ManagedAccountService from '@/entities/managed-account/managed-account.service';
import { IManagedAccount } from '@/shared/model/managed-account.model';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { IAccountReclaimPayout, AccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';
import AccountReclaimPayoutService from './account-reclaim-payout.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  accountReclaimPayout: {
    amount: {
      required,
      decimal,
    },
    timestamp: {
      required,
    },
    ccy: {
      required,
    },
    txnRef: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class AccountReclaimPayoutUpdate extends Vue {
  @Inject('accountReclaimPayoutService') private accountReclaimPayoutService: () => AccountReclaimPayoutService;
  @Inject('alertService') private alertService: () => AlertService;

  public accountReclaimPayout: IAccountReclaimPayout = new AccountReclaimPayout();

  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;

  public managedAccounts: IManagedAccount[] = [];

  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;

  public accountReclaimRequests: IAccountReclaimRequest[] = [];

  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;

  public voteManagers: IVoteManager[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.accountReclaimPayoutId) {
        vm.retrieveAccountReclaimPayout(to.params.accountReclaimPayoutId);
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
    if (this.accountReclaimPayout.id) {
      this.accountReclaimPayoutService()
        .update(this.accountReclaimPayout)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.accountReclaimPayout.updated', { param: param.id });
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
      this.accountReclaimPayoutService()
        .create(this.accountReclaimPayout)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.accountReclaimPayout.created', { param: param.id });
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
      this.accountReclaimPayout[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.accountReclaimPayout[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.accountReclaimPayout[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.accountReclaimPayout[field] = null;
    }
  }

  public retrieveAccountReclaimPayout(accountReclaimPayoutId): void {
    this.accountReclaimPayoutService()
      .find(accountReclaimPayoutId)
      .then(res => {
        res.timestamp = new Date(res.timestamp);
        this.accountReclaimPayout = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.managedAccountService()
      .retrieve()
      .then(res => {
        this.managedAccounts = res.data;
      });
    this.accountReclaimRequestService()
      .retrieve()
      .then(res => {
        this.accountReclaimRequests = res.data;
      });
    this.voteManagerService()
      .retrieve()
      .then(res => {
        this.voteManagers = res.data;
      });
  }
}
