<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voterPreferences.home.createOrEditLabel"
          data-cy="VoterPreferencesCreateUpdateHeading"
          v-text="$t('upraizApp.voterPreferences.home.createOrEditLabel')"
        >
          Create or edit a VoterPreferences
        </h2>
        <div>
          <div class="form-group" v-if="voterPreferences.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voterPreferences.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voterPreferences.receiveCcy')" for="voter-preferences-receiveCcy"
              >Receive Ccy</label
            >
            <select
              class="form-control"
              name="receiveCcy"
              :class="{ valid: !$v.voterPreferences.receiveCcy.$invalid, invalid: $v.voterPreferences.receiveCcy.$invalid }"
              v-model="$v.voterPreferences.receiveCcy.$model"
              id="voter-preferences-receiveCcy"
              data-cy="receiveCcy"
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
            :disabled="$v.voterPreferences.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./voter-preferences-update.component.ts"></script>
