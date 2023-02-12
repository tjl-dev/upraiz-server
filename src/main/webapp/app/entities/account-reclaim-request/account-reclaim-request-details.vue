<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="accountReclaimRequest">
        <h2 class="jh-entity-heading" data-cy="accountReclaimRequestDetailsHeading">
          <span v-text="$t('upraizApp.accountReclaimRequest.detail.title')">AccountReclaimRequest</span> {{ accountReclaimRequest.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ accountReclaimRequest.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.timestamp')">Timestamp</span>
          </dt>
          <dd>
            <span v-if="accountReclaimRequest.timestamp">{{ $d(Date.parse(accountReclaimRequest.timestamp), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.ccy')">Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + accountReclaimRequest.ccy)">{{ accountReclaimRequest.ccy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.active')">Active</span>
          </dt>
          <dd>
            <span>{{ accountReclaimRequest.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.accountReclaimPayout')">Account Reclaim Payout</span>
          </dt>
          <dd>
            <div v-if="accountReclaimRequest.accountReclaimPayout">
              <router-link
                :to="{
                  name: 'AccountReclaimPayoutView',
                  params: { accountReclaimPayoutId: accountReclaimRequest.accountReclaimPayout.id },
                }"
                >{{ accountReclaimRequest.accountReclaimPayout.id }}</router-link
              >
            </div>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.managedAccount')">Managed Account</span>
          </dt>
          <dd>
            <div v-if="accountReclaimRequest.managedAccount">
              <router-link :to="{ name: 'ManagedAccountView', params: { managedAccountId: accountReclaimRequest.managedAccount.id } }">{{
                accountReclaimRequest.managedAccount.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.accountReclaimRequest.voteManager')">Vote Manager</span>
          </dt>
          <dd>
            <div v-if="accountReclaimRequest.voteManager">
              <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: accountReclaimRequest.voteManager.id } }">{{
                accountReclaimRequest.voteManager.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="accountReclaimRequest.id"
          :to="{ name: 'AccountReclaimRequestEdit', params: { accountReclaimRequestId: accountReclaimRequest.id } }"
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

<script lang="ts" src="./account-reclaim-request-details.component.ts"></script>
