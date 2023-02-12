import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const VoteManager = () => import('@/entities/vote-manager/vote-manager.vue');
const VoteManagerUpdate = () => import('@/entities/vote-manager/vote-manager-update.vue');
const VoteManagerDetails = () => import('@/entities/vote-manager/vote-manager-details.vue');

const VoteManagerPreferences = () => import('@/entities/vote-manager-preferences/vote-manager-preferences.vue');
const VoteManagerPreferencesUpdate = () => import('@/entities/vote-manager-preferences/vote-manager-preferences-update.vue');
const VoteManagerPreferencesDetails = () => import('@/entities/vote-manager-preferences/vote-manager-preferences-details.vue');

const Voter = () => import('@/entities/voter/voter.vue');
const VoterUpdate = () => import('@/entities/voter/voter-update.vue');
const VoterDetails = () => import('@/entities/voter/voter-details.vue');

const VoterPreferences = () => import('@/entities/voter-preferences/voter-preferences.vue');
const VoterPreferencesUpdate = () => import('@/entities/voter-preferences/voter-preferences-update.vue');
const VoterPreferencesDetails = () => import('@/entities/voter-preferences/voter-preferences-details.vue');

const ManagedAccount = () => import('@/entities/managed-account/managed-account.vue');
const ManagedAccountUpdate = () => import('@/entities/managed-account/managed-account-update.vue');
const ManagedAccountDetails = () => import('@/entities/managed-account/managed-account-details.vue');

const VoterAccount = () => import('@/entities/voter-account/voter-account.vue');
const VoterAccountUpdate = () => import('@/entities/voter-account/voter-account-update.vue');
const VoterAccountDetails = () => import('@/entities/voter-account/voter-account-details.vue');

const VoteTarget = () => import('@/entities/vote-target/vote-target.vue');
const VoteTargetUpdate = () => import('@/entities/vote-target/vote-target-update.vue');
const VoteTargetDetails = () => import('@/entities/vote-target/vote-target-details.vue');

const Vote = () => import('@/entities/vote/vote.vue');
const VoteUpdate = () => import('@/entities/vote/vote-update.vue');
const VoteDetails = () => import('@/entities/vote/vote-details.vue');

const VotePayout = () => import('@/entities/vote-payout/vote-payout.vue');
const VotePayoutUpdate = () => import('@/entities/vote-payout/vote-payout-update.vue');
const VotePayoutDetails = () => import('@/entities/vote-payout/vote-payout-details.vue');

const AccountReclaimRequest = () => import('@/entities/account-reclaim-request/account-reclaim-request.vue');
const AccountReclaimRequestUpdate = () => import('@/entities/account-reclaim-request/account-reclaim-request-update.vue');
const AccountReclaimRequestDetails = () => import('@/entities/account-reclaim-request/account-reclaim-request-details.vue');

const AccountReclaimPayout = () => import('@/entities/account-reclaim-payout/account-reclaim-payout.vue');
const AccountReclaimPayoutUpdate = () => import('@/entities/account-reclaim-payout/account-reclaim-payout-update.vue');
const AccountReclaimPayoutDetails = () => import('@/entities/account-reclaim-payout/account-reclaim-payout-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'vote-manager',
      name: 'VoteManager',
      component: VoteManager,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager/new',
      name: 'VoteManagerCreate',
      component: VoteManagerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager/:voteManagerId/edit',
      name: 'VoteManagerEdit',
      component: VoteManagerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager/:voteManagerId/view',
      name: 'VoteManagerView',
      component: VoteManagerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager-preferences',
      name: 'VoteManagerPreferences',
      component: VoteManagerPreferences,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager-preferences/new',
      name: 'VoteManagerPreferencesCreate',
      component: VoteManagerPreferencesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager-preferences/:voteManagerPreferencesId/edit',
      name: 'VoteManagerPreferencesEdit',
      component: VoteManagerPreferencesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-manager-preferences/:voteManagerPreferencesId/view',
      name: 'VoteManagerPreferencesView',
      component: VoteManagerPreferencesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter',
      name: 'Voter',
      component: Voter,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter/new',
      name: 'VoterCreate',
      component: VoterUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter/:voterId/edit',
      name: 'VoterEdit',
      component: VoterUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter/:voterId/view',
      name: 'VoterView',
      component: VoterDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-preferences',
      name: 'VoterPreferences',
      component: VoterPreferences,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-preferences/new',
      name: 'VoterPreferencesCreate',
      component: VoterPreferencesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-preferences/:voterPreferencesId/edit',
      name: 'VoterPreferencesEdit',
      component: VoterPreferencesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-preferences/:voterPreferencesId/view',
      name: 'VoterPreferencesView',
      component: VoterPreferencesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'managed-account',
      name: 'ManagedAccount',
      component: ManagedAccount,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'managed-account/new',
      name: 'ManagedAccountCreate',
      component: ManagedAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'managed-account/:managedAccountId/edit',
      name: 'ManagedAccountEdit',
      component: ManagedAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'managed-account/:managedAccountId/view',
      name: 'ManagedAccountView',
      component: ManagedAccountDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-account',
      name: 'VoterAccount',
      component: VoterAccount,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-account/new',
      name: 'VoterAccountCreate',
      component: VoterAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-account/:voterAccountId/edit',
      name: 'VoterAccountEdit',
      component: VoterAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'voter-account/:voterAccountId/view',
      name: 'VoterAccountView',
      component: VoterAccountDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-target',
      name: 'VoteTarget',
      component: VoteTarget,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-target/new',
      name: 'VoteTargetCreate',
      component: VoteTargetUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-target/:voteTargetId/edit',
      name: 'VoteTargetEdit',
      component: VoteTargetUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-target/:voteTargetId/view',
      name: 'VoteTargetView',
      component: VoteTargetDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote',
      name: 'Vote',
      component: Vote,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote/new',
      name: 'VoteCreate',
      component: VoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote/:voteId/edit',
      name: 'VoteEdit',
      component: VoteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote/:voteId/view',
      name: 'VoteView',
      component: VoteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-payout',
      name: 'VotePayout',
      component: VotePayout,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-payout/new',
      name: 'VotePayoutCreate',
      component: VotePayoutUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-payout/:votePayoutId/edit',
      name: 'VotePayoutEdit',
      component: VotePayoutUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'vote-payout/:votePayoutId/view',
      name: 'VotePayoutView',
      component: VotePayoutDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-request',
      name: 'AccountReclaimRequest',
      component: AccountReclaimRequest,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-request/new',
      name: 'AccountReclaimRequestCreate',
      component: AccountReclaimRequestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-request/:accountReclaimRequestId/edit',
      name: 'AccountReclaimRequestEdit',
      component: AccountReclaimRequestUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-request/:accountReclaimRequestId/view',
      name: 'AccountReclaimRequestView',
      component: AccountReclaimRequestDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-payout',
      name: 'AccountReclaimPayout',
      component: AccountReclaimPayout,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-payout/new',
      name: 'AccountReclaimPayoutCreate',
      component: AccountReclaimPayoutUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-payout/:accountReclaimPayoutId/edit',
      name: 'AccountReclaimPayoutEdit',
      component: AccountReclaimPayoutUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'account-reclaim-payout/:accountReclaimPayoutId/view',
      name: 'AccountReclaimPayoutView',
      component: AccountReclaimPayoutDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
