<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="accountReclaimPayout">
        <h2 class="jh-entity-heading" data-cy="accountReclaimPayoutDetailsHeading">
          <span v-text="$t('upraizApp.accountReclaimPayout.detail.title')">AccountReclaimPayout</span> {{ accountReclaimPayout.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ accountReclaimPayout.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.timestamp')">Timestamp</span>
          </dt>
          <dd>
            <span v-if="accountReclaimPayout.timestamp">{{ $d(Date.parse(accountReclaimPayout.timestamp), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.ccy')">Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + accountReclaimPayout.ccy)">{{ accountReclaimPayout.ccy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.txnRef')">Txn Ref</span>
          </dt>
          <dd>
            <span>{{ accountReclaimPayout.txnRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.managedAccount')">Managed Account</span>
          </dt>
          <dd>
            <div v-if="accountReclaimPayout.managedAccount">
              <router-link :to="{ name: 'ManagedAccountView', params: { managedAccountId: accountReclaimPayout.managedAccount.id } }">{{
                accountReclaimPayout.managedAccount.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimPayout.voteManager')">Vote Manager</span>
          </dt>
          <dd>
            <div v-if="accountReclaimPayout.voteManager">
              <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: accountReclaimPayout.voteManager.id } }">{{
                accountReclaimPayout.voteManager.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="accountReclaimPayout.id"
          :to="{ name: 'AccountReclaimPayoutEdit', params: { accountReclaimPayoutId: accountReclaimPayout.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./account-reclaim-payout-details.component.ts"></script>
