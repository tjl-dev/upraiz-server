<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="voteTarget">
        <h2 class="jh-entity-heading" data-cy="voteTargetDetailsHeading">
          <span v-text="$t('upraizApp.voteTarget.detail.title')">VoteTarget</span> {{ voteTarget.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.voteTarget.url')">Url</span>
          </dt>
          <dd>
            <span>{{ voteTarget.url }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.votetype')">Votetype</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteTargetType.' + voteTarget.votetype)">{{ voteTarget.votetype }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.payout')">Payout</span>
          </dt>
          <dd>
            <span>{{ voteTarget.payout }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.ccy')">Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + voteTarget.ccy)">{{ voteTarget.ccy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.comment')">Comment</span>
          </dt>
          <dd>
            <span>{{ voteTarget.comment }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.active')">Active</span>
          </dt>
          <dd>
            <span>{{ voteTarget.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.funded')">Funded</span>
          </dt>
          <dd>
            <span>{{ voteTarget.funded }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.created')">Created</span>
          </dt>
          <dd>
            <span v-if="voteTarget.created">{{ $d(Date.parse(voteTarget.created), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.expiry')">Expiry</span>
          </dt>
          <dd>
            <span v-if="voteTarget.expiry">{{ $d(Date.parse(voteTarget.expiry), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.boosted')">Boosted</span>
          </dt>
          <dd>
            <span>{{ voteTarget.boosted }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteTarget.voteManager')">Vote Manager</span>
          </dt>
          <dd>
            <div v-if="voteTarget.voteManager">
              <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: voteTarget.voteManager.id } }">{{
                voteTarget.voteManager.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="voteTarget.id"
          :to="{ name: 'VoteTargetEdit', params: { voteTargetId: voteTarget.id } }"
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

<script lang="ts" src="./vote-target-details.component.ts"></script>
