<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="voterAccount">
        <h2 class="jh-entity-heading" data-cy="voterAccountDetailsHeading">
          <span v-text="$t('upraizApp.voterAccount.detail.title')">VoterAccount</span> {{ voterAccount.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.voterAccount.accountname')">Accountname</span>
          </dt>
          <dd>
            <span>{{ voterAccount.accountname }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voterAccount.ccy')">Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + voterAccount.ccy)">{{ voterAccount.ccy }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voterAccount.address')">Address</span>
          </dt>
          <dd>
            <span>{{ voterAccount.address }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voterAccount.voter')">Voter</span>
          </dt>
          <dd>
            <div v-if="voterAccount.voter">
              <router-link :to="{ name: 'VoterView', params: { voterId: voterAccount.voter.id } }">{{ voterAccount.voter.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="voterAccount.id"
          :to="{ name: 'VoterAccountEdit', params: { voterAccountId: voterAccount.id } }"
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

<script lang="ts" src="./voter-account-details.component.ts"></script>
