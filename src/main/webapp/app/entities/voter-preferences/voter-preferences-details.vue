<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="voterPreferences">
        <h2 class="jh-entity-heading" data-cy="voterPreferencesDetailsHeading">
          <span v-text="$t('upraizApp.voterPreferences.detail.title')">VoterPreferences</span> {{ voterPreferences.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.voterPreferences.receiveCcy')">Receive Ccy</span>
          </dt>
          <dd>
            <span v-text="$t('upraizApp.VoteCcy.' + voterPreferences.receiveCcy)">{{ voterPreferences.receiveCcy }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="voterPreferences.id"
          :to="{ name: 'VoterPreferencesEdit', params: { voterPreferencesId: voterPreferences.id } }"
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

<script lang="ts" src="./voter-preferences-details.component.ts"></script>
