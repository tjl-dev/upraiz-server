<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.managedAccount.home.createOrEditLabel"
          data-cy="ManagedAccountCreateUpdateHeading"
          v-text="$t('upraizApp.managedAccount.home.createOrEditLabel')"
        >
          Create or edit a ManagedAccount
        </h2>
        <div>
          <div class="form-group" v-if="managedAccount.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="managedAccount.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.managedAccount.accountName')" for="managed-account-accountName"
              >Account Name</label
            >
            <input
              type="text"
              class="form-control"
              name="accountName"
              id="managed-account-accountName"
              data-cy="accountName"
              :class="{ valid: !$v.managedAccount.accountName.$invalid, invalid: $v.managedAccount.accountName.$invalid }"
              v-model="$v.managedAccount.accountName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.managedAccount.ccy')" for="managed-account-ccy">Ccy</label>
            <select
              class="form-control"
              name="ccy"
              :class="{ valid: !$v.managedAccount.ccy.$invalid, invalid: $v.managedAccount.ccy.$invalid }"
              v-model="$v.managedAccount.ccy.$model"
              id="managed-account-ccy"
              data-cy="ccy"
              required
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
            <div v-if="$v.managedAccount.ccy.$anyDirty && $v.managedAccount.ccy.$invalid">
              <small class="form-text text-danger" v-if="!$v.managedAccount.ccy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.managedAccount.address')" for="managed-account-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="managed-account-address"
              data-cy="address"
              :class="{ valid: !$v.managedAccount.address.$invalid, invalid: $v.managedAccount.address.$invalid }"
              v-model="$v.managedAccount.address.$model"
              required
            />
            <div v-if="$v.managedAccount.address.$anyDirty && $v.managedAccount.address.$invalid">
              <small class="form-text text-danger" v-if="!$v.managedAccount.address.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.managedAccount.seed')" for="managed-account-seed">Seed</label>
            <input
              type="text"
              class="form-control"
              name="seed"
              id="managed-account-seed"
              data-cy="seed"
              :class="{ valid: !$v.managedAccount.seed.$invalid, invalid: $v.managedAccount.seed.$invalid }"
              v-model="$v.managedAccount.seed.$model"
              required
            />
            <div v-if="$v.managedAccount.seed.$anyDirty && $v.managedAccount.seed.$invalid">
              <small class="form-text text-danger" v-if="!$v.managedAccount.seed.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.managedAccount.voteManager')" for="managed-account-voteManager"
              >Vote Manager</label
            >
            <select
              class="form-control"
              id="managed-account-voteManager"
              data-cy="voteManager"
              name="voteManager"
              v-model="managedAccount.voteManager"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  managedAccount.voteManager && voteManagerOption.id === managedAccount.voteManager.id
                    ? managedAccount.voteManager
                    : voteManagerOption
                "
                v-for="voteManagerOption in voteManagers"
                :key="voteManagerOption.id"
              >
                {{ voteManagerOption.id }}
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
            :disabled="$v.managedAccount.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./managed-account-update.component.ts"></script>
