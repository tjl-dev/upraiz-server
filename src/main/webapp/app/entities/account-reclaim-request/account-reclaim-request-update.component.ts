import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import AccountReclaimPayoutService from '@/entities/account-reclaim-payout/account-reclaim-payout.service';
import { IAccountReclaimPayout } from '@/shared/model/account-reclaim-payout.model';

import ManagedAccountService from '@/entities/managed-account/managed-account.service';
import { IManagedAccount } from '@/shared/model/managed-account.model';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import { IAccountReclaimRequest, AccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';
import AccountReclaimRequestService from './account-reclaim-request.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  accountReclaimRequest: {
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
    active: {},
  },
};

@Component({
  validations,
})
export default class AccountReclaimRequestUpdate extends Vue {
  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;
  @Inject('alertService') private alertService: () => AlertService;

  public accountReclaimRequest: IAccountReclaimRequest = new AccountReclaimRequest();

  @Inject('accountReclaimPayoutService') private accountReclaimPayoutService: () => AccountReclaimPayoutService;

  public accountReclaimPayouts: IAccountReclaimPayout[] = [];

  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;

  public managedAccounts: IManagedAccount[] = [];

  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;

  public voteManagers: IVoteManager[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.accountReclaimRequestId) {
        vm.retrieveAccountReclaimRequest(to.params.accountReclaimRequestId);
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
    if (this.accountReclaimRequest.id) {
      this.accountReclaimRequestService()
        .update(this.accountReclaimRequest)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.accountReclaimRequest.updated', { param: param.id });
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
      this.accountReclaimRequestService()
        .create(this.accountReclaimRequest)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.accountReclaimRequest.created', { param: param.id });
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
      this.accountReclaimRequest[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.accountReclaimRequest[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.accountReclaimRequest[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.accountReclaimRequest[field] = null;
    }
  }

  public retrieveAccountReclaimRequest(accountReclaimRequestId): void {
    this.accountReclaimRequestService()
      .find(accountReclaimRequestId)
      .then(res => {
        res.timestamp = new Date(res.timestamp);
        this.accountReclaimRequest = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.accountReclaimPayoutService()
      .retrieve()
      .then(res => {
        this.accountReclaimPayouts = res.data;
      });
    this.managedAccountService()
      .retrieve()
      .then(res => {
        this.managedAccounts = res.data;
      });
    this.voteManagerService()
      .retrieve()
      .then(res => {
        this.voteManagers = res.data;
      });
  }
}
