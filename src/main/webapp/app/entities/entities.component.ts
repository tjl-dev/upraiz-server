import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import VoteManagerService from './vote-manager/vote-manager.service';
import VoteManagerPreferencesService from './vote-manager-preferences/vote-manager-preferences.service';
import VoterService from './voter/voter.service';
import VoterPreferencesService from './voter-preferences/voter-preferences.service';
import ManagedAccountService from './managed-account/managed-account.service';
import VoterAccountService from './voter-account/voter-account.service';
import VoteTargetService from './vote-target/vote-target.service';
import VoteService from './vote/vote.service';
import VotePayoutService from './vote-payout/vote-payout.service';
import AccountReclaimRequestService from './account-reclaim-request/account-reclaim-request.service';
import AccountReclaimPayoutService from './account-reclaim-payout/account-reclaim-payout.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('voteManagerService') private voteManagerService = () => new VoteManagerService();
  @Provide('voteManagerPreferencesService') private voteManagerPreferencesService = () => new VoteManagerPreferencesService();
  @Provide('voterService') private voterService = () => new VoterService();
  @Provide('voterPreferencesService') private voterPreferencesService = () => new VoterPreferencesService();
  @Provide('managedAccountService') private managedAccountService = () => new ManagedAccountService();
  @Provide('voterAccountService') private voterAccountService = () => new VoterAccountService();
  @Provide('voteTargetService') private voteTargetService = () => new VoteTargetService();
  @Provide('voteService') private voteService = () => new VoteService();
  @Provide('votePayoutService') private votePayoutService = () => new VotePayoutService();
  @Provide('accountReclaimRequestService') private accountReclaimRequestService = () => new AccountReclaimRequestService();
  @Provide('accountReclaimPayoutService') private accountReclaimPayoutService = () => new AccountReclaimPayoutService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
