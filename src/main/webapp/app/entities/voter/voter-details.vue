<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="voter">
        <h2 class="jh-entity-heading" data-cy="voterDetailsHeading">
          <span v-text="$t('upraizApp.voter.detail.title')">Voter</span> {{ voter.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.voter.email')">Email</span>
          </dt>
          <dd>
            <span>{{ voter.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voter.name')">Name</span>
          </dt>
          <dd>
            <span>{{ voter.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voter.created')">Created</span>
          </dt>
          <dd>
            <span v-if="voter.created">{{ $d(Date.parse(voter.created), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voter.active')">Active</span>
          </dt>
          <dd>
            <span>{{ voter.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voter.comment')">Comment</span>
          </dt>
          <dd>
            <span>{{ voter.comment }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voter.voterPreferences')">Voter Preferences</span>
          </dt>
          <dd>
            <div v-if="voter.voterPreferences">
              <router-link :to="{ name: 'VoterPreferencesView', params: { voterPreferencesId: voter.voterPreferences.id } }">{{
                voter.voterPreferences.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="voter.id" :to="{ name: 'VoterEdit', params: { voterId: voter.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./voter-details.component.ts"></script>
