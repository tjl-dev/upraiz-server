<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voteManagerPreferences.home.createOrEditLabel"
          data-cy="VoteManagerPreferencesCreateUpdateHeading"
          v-text="$t('upraizApp.voteManagerPreferences.home.createOrEditLabel')"
        >
          Create or edit a VoteManagerPreferences
        </h2>
        <div>
          <div class="form-group" v-if="voteManagerPreferences.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voteManagerPreferences.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManagerPreferences.payCcy')" for="vote-manager-preferences-payCcy"
              >Pay Ccy</label
            >
            <select
              class="form-control"
              name="payCcy"
              :class="{ valid: !$v.voteManagerPreferences.payCcy.$invalid, invalid: $v.voteManagerPreferences.payCcy.$invalid }"
              v-model="$v.voteManagerPreferences.payCcy.$model"
              id="vote-manager-preferences-payCcy"
              data-cy="payCcy"
            >
              <option
                v-for="voteCcy in voteCcyValues"
                :key="voteCcy"
                v-bind:value="voteCcy"
                v-bind:label="$t('upraizApp.VoteCcy.' + voteCcy)"
              >
                {{ voteCcy }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.voteManagerPreferences.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./vote-manager-preferences-update.component.ts"></script>
