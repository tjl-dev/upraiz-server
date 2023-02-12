<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="votePayout">
        <h2 class="jh-entity-heading" data-cy="votePayoutDetailsHeading">
          <span v-text="$t('upraizApp.votePayout.detail.title')">VotePayout</span> {{ votePayout.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.votePayout.paidTime')">Paid Time</span>
          </dt>
          <dd>
            <span v-if="votePayout.paidTime">{{ $d(Date.parse(votePayout.paidTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.votePayout.paidAmount')">Paid Amount</span>
          </dt>
          <dd>
            <span>{{ votePayout.paidAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.votePayout.paidCcy')">Paid Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + votePayout.paidCcy)">{{ votePayout.paidCcy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.votePayout.voterAccount')">Voter Account</span>
          </dt>
          <dd>
            <div v-if="votePayout.voterAccount">
              <router-link :to="{ name: 'VoterAccountView', params: { voterAccountId: votePayout.voterAccount.id } }">{{
                votePayout.voterAccount.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="votePayout.id"
          :to="{ name: 'VotePayoutEdit', params: { votePayoutId: votePayout.id } }"
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

<script lang="ts" src="./vote-payout-details.component.ts"></script>
