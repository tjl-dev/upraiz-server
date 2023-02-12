<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="voteManager">
        <h2 class="jh-entity-heading" data-cy="voteManagerDetailsHeading">
          <span v-text="$t('upraizApp.voteManager.detail.title')">VoteManager</span> {{ voteManager.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('upraizApp.voteManager.email')">Email</span>
          </dt>
          <dd>
            <span>{{ voteManager.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteManager.name')">Name</span>
          </dt>
          <dd>
            <span>{{ voteManager.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteManager.created')">Created</span>
          </dt>
          <dd>
            <span v-if="voteManager.created">{{ $d(Date.parse(voteManager.created), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteManager.active')">Active</span>
          </dt>
          <dd>
            <span>{{ voteManager.active }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteManager.comment')">Comment</span>
          </dt>
          <dd>
            <span>{{ voteManager.comment }}</span>
          </dd>
          <dt>
            <span v-text="$t('upraizApp.voteManager.voteManagerPreferences')">Vote Manager Preferences</span>
          </dt>
          <dd>
            <div v-if="voteManager.voteManagerPreferences">
              <router-link
                :to="{ name: 'VoteManagerPreferencesView', params: { voteManagerPreferencesId: voteManager.voteManagerPreferences.id } }"
                >{{ voteManager.voteManagerPreferences.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="voteManager.id"
          :to="{ name: 'VoteManagerEdit', params: { voteManagerId: voteManager.id } }"
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

<script lang="ts" src="./vote-manager-details.component.ts"></script>
