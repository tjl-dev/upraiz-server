<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voter.home.createOrEditLabel"
          data-cy="VoterCreateUpdateHeading"
          v-text="$t('upraizApp.voter.home.createOrEditLabel')"
        >
          Create or edit a Voter
        </h2>
        <div>
          <div class="form-group" v-if="voter.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voter.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.email')" for="voter-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="voter-email"
              data-cy="email"
              :class="{ valid: !$v.voter.email.$invalid, invalid: $v.voter.email.$invalid }"
              v-model="$v.voter.email.$model"
              required
            />
            <div v-if="$v.voter.email.$anyDirty && $v.voter.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.voter.email.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.name')" for="voter-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="voter-name"
              data-cy="name"
              :class="{ valid: !$v.voter.name.$invalid, invalid: $v.voter.name.$invalid }"
              v-model="$v.voter.name.$model"
              required
            />
            <div v-if="$v.voter.name.$anyDirty && $v.voter.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.voter.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.created')" for="voter-created">Created</label>
            <div class="d-flex">
              <input
                id="voter-created"
                data-cy="created"
                type="datetime-local"
                class="form-control"
                name="created"
                :class="{ valid: !$v.voter.created.$invalid, invalid: $v.voter.created.$invalid }"
                :value="convertDateTimeFromServer($v.voter.created.$model)"
                @change="updateZonedDateTimeField('created', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.active')" for="voter-active">Active</label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="voter-active"
              data-cy="active"
              :class="{ valid: !$v.voter.active.$invalid, invalid: $v.voter.active.$invalid }"
              v-model="$v.voter.active.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.comment')" for="voter-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="voter-comment"
              data-cy="comment"
              :class="{ valid: !$v.voter.comment.$invalid, invalid: $v.voter.comment.$invalid }"
              v-model="$v.voter.comment.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voter.voterPreferences')" for="voter-voterPreferences"
              >Voter Preferences</label
            >
            <select
              class="form-control"
              id="voter-voterPreferences"
              data-cy="voterPreferences"
              name="voterPreferences"
              v-model="voter.voterPreferences"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  voter.voterPreferences && voterPreferencesOption.id === voter.voterPreferences.id
                    ? voter.voterPreferences
                    : voterPreferencesOption
                "
                v-for="voterPreferencesOption in voterPreferences"
                :key="voterPreferencesOption.id"
              >
                {{ voterPreferencesOption.id }}
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
            :disabled="$v.voter.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./voter-update.component.ts"></script>
