import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import VoteManagerService from '@/entities/vote-manager/vote-manager.service';
import { IVoteManager } from '@/shared/model/vote-manager.model';

import AccountReclaimRequestService from '@/entities/account-reclaim-request/account-reclaim-request.service';
import { IAccountReclaimRequest } from '@/shared/model/account-reclaim-request.model';

import { IManagedAccount, ManagedAccount } from '@/shared/model/managed-account.model';
import ManagedAccountService from './managed-account.service';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';

const validations: any = {
  managedAccount: {
    accountName: {},
    ccy: {
      required,
    },
    address: {
      required,
    },
    seed: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class ManagedAccountUpdate extends Vue {
  @Inject('managedAccountService') private managedAccountService: () => ManagedAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public managedAccount: IManagedAccount = new ManagedAccount();

  @Inject('voteManagerService') private voteManagerService: () => VoteManagerService;

  public voteManagers: IVoteManager[] = [];

  @Inject('accountReclaimRequestService') private accountReclaimRequestService: () => AccountReclaimRequestService;

  public accountReclaimRequests: IAccountReclaimRequest[] = [];
  public voteCcyValues: string[] = Object.keys(VoteCcy);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.managedAccountId) {
        vm.retrieveManagedAccount(to.params.managedAccountId);
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
    if (this.managedAccount.id) {
      this.managedAccountService()
        .update(this.managedAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.managedAccount.updated', { param: param.id });
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
      this.managedAccountService()
        .create(this.managedAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('upraizApp.managedAccount.created', { param: param.id });
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

  public retrieveManagedAccount(managedAccountId): void {
    this.managedAccountService()
      .find(managedAccountId)
      .then(res => {
        this.managedAccount = res;
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
    this.accountReclaimRequestService()
      .retrieve()
      .then(res => {
        this.accountReclaimRequests = res.data;
      });
  }
}
