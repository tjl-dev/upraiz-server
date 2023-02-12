<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="managedAccount">
        <h2 class="jh-entity-heading" data-cy="managedAccountDetailsHeading">
          <span v-text="$t('upraizApp.managedAccount.detail.title')">ManagedAccount</span> {{ managedAccount.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.managedAccount.accountName')">Account Name</span>
          </dt>
          <dd>
            <span>{{ managedAccount.accountName }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.managedAccount.ccy')">Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + managedAccount.ccy)">{{ managedAccount.ccy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.managedAccount.address')">Address</span>
          </dt>
          <dd>
            <span>{{ managedAccount.address }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.managedAccount.seed')">Seed</span>
          </dt>
          <dd>
            <span>{{ managedAccount.seed }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.managedAccount.voteManager')">Vote Manager</span>
          </dt>
          <dd>
            <div v-if="managedAccount.voteManager">
              <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: managedAccount.voteManager.id } }">{{
                managedAccount.voteManager.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="managedAccount.id"
          :to="{ name: 'ManagedAccountEdit', params: { managedAccountId: managedAccount.id } }"
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

<script lang="ts" src="./managed-account-details.component.ts"></script>
