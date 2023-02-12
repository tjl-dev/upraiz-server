<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voteManager.home.createOrEditLabel"
          data-cy="VoteManagerCreateUpdateHeading"
          v-text="$t('upraizApp.voteManager.home.createOrEditLabel')"
        >
          Create or edit a VoteManager
        </h2>
        <div>
          <div class="form-group" v-if="voteManager.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voteManager.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManager.email')" for="vote-manager-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="vote-manager-email"
              data-cy="email"
              :class="{ valid: !$v.voteManager.email.$invalid, invalid: $v.voteManager.email.$invalid }"
              v-model="$v.voteManager.email.$model"
              required
            />
            <div v-if="$v.voteManager.email.$anyDirty && $v.voteManager.email.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteManager.email.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManager.name')" for="vote-manager-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="vote-manager-name"
              data-cy="name"
              :class="{ valid: !$v.voteManager.name.$invalid, invalid: $v.voteManager.name.$invalid }"
              v-model="$v.voteManager.name.$model"
              required
            />
            <div v-if="$v.voteManager.name.$anyDirty && $v.voteManager.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteManager.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManager.created')" for="vote-manager-created">Created</label>
            <div class="d-flex">
              <input
                id="vote-manager-created"
                data-cy="created"
                type="datetime-local"
                class="form-control"
                name="created"
                :class="{ valid: !$v.voteManager.created.$invalid, invalid: $v.voteManager.created.$invalid }"
                :value="convertDateTimeFromServer($v.voteManager.created.$model)"
                @change="updateZonedDateTimeField('created', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManager.active')" for="vote-manager-active">Active</label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="vote-manager-active"
              data-cy="active"
              :class="{ valid: !$v.voteManager.active.$invalid, invalid: $v.voteManager.active.$invalid }"
              v-model="$v.voteManager.active.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteManager.comment')" for="vote-manager-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="vote-manager-comment"
              data-cy="comment"
              :class="{ valid: !$v.voteManager.comment.$invalid, invalid: $v.voteManager.comment.$invalid }"
              v-model="$v.voteManager.comment.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.voteManager.voteManagerPreferences')"
              for="vote-manager-voteManagerPreferences"
              >Vote Manager Preferences</label
            >
            <select
              class="form-control"
              id="vote-manager-voteManagerPreferences"
              data-cy="voteManagerPreferences"
              name="voteManagerPreferences"
              v-model="voteManager.voteManagerPreferences"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  voteManager.voteManagerPreferences && voteManagerPreferencesOption.id === voteManager.voteManagerPreferences.id
                    ? voteManager.voteManagerPreferences
                    : voteManagerPreferencesOption
                "
                v-for="voteManagerPreferencesOption in voteManagerPreferences"
                :key="voteManagerPreferencesOption.id"
              >
                {{ voteManagerPreferencesOption.id }}
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
            :disabled="$v.voteManager.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./vote-manager-update.component.ts"></script>
