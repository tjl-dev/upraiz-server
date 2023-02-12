<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="vote">
        <h2 class="jh-entity-heading" data-cy="voteDetailsHeading">
          <span v-text="$t('upraizApp.vote.detail.title')">Vote</span> {{ vote.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.vote.votedTimestamp')">Voted Timestamp</span>
          </dt>
          <dd>
            <span v-if="vote.votedTimestamp">{{ $d(Date.parse(vote.votedTimestamp), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.verified')">Verified</span>
          </dt>
          <dd>
            <span>{{ vote.verified }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.verifiedTime')">Verified Time</span>
          </dt>
          <dd>
            <span v-if="vote.verifiedTime">{{ $d(Date.parse(vote.verifiedTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.verifiedBy')">Verified By</span>
          </dt>
          <dd>
            <span>{{ vote.verifiedBy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.paid')">Paid</span>
          </dt>
          <dd>
            <span>{{ vote.paid }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.voteTarget')">Vote Target</span>
          </dt>
          <dd>
            <div v-if="vote.voteTarget">
              <router-link :to="{ name: 'VoteTargetView', params: { voteTargetId: vote.voteTarget.id } }">{{
                vote.voteTarget.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.votePayout')">Vote Payout</span>
          </dt>
          <dd>
            <div v-if="vote.votePayout">
              <router-link :to="{ name: 'VotePayoutView', params: { votePayoutId: vote.votePayout.id } }">{{
                vote.votePayout.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.vote.voter')">Voter</span>
          </dt>
          <dd>
            <div v-if="vote.voter">
              <router-link :to="{ name: 'VoterView', params: { voterId: vote.voter.id } }">{{ vote.voter.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="vote.id" :to="{ name: 'VoteEdit', params: { voteId: vote.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./vote-details.component.ts"></script>
