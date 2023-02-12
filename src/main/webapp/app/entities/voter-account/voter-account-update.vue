<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voterAccount.home.createOrEditLabel"
          data-cy="VoterAccountCreateUpdateHeading"
          v-text="$t('upraizApp.voterAccount.home.createOrEditLabel')"
        >
          Create or edit a VoterAccount
        </h2>
        <div>
          <div class="form-group" v-if="voterAccount.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voterAccount.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voterAccount.accountname')" for="voter-account-accountname"
              >Accountname</label
            >
            <input
              type="text"
              class="form-control"
              name="accountname"
              id="voter-account-accountname"
              data-cy="accountname"
              :class="{ valid: !$v.voterAccount.accountname.$invalid, invalid: $v.voterAccount.accountname.$invalid }"
              v-model="$v.voterAccount.accountname.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voterAccount.ccy')" for="voter-account-ccy">Ccy</label>
            <select
              class="form-control"
              name="ccy"
              :class="{ valid: !$v.voterAccount.ccy.$invalid, invalid: $v.voterAccount.ccy.$invalid }"
              v-model="$v.voterAccount.ccy.$model"
              id="voter-account-ccy"
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
            <div v-if="$v.voterAccount.ccy.$anyDirty && $v.voterAccount.ccy.$invalid">
              <small class="form-text text-danger" v-if="!$v.voterAccount.ccy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voterAccount.address')" for="voter-account-address">Address</label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="voter-account-address"
              data-cy="address"
              :class="{ valid: !$v.voterAccount.address.$invalid, invalid: $v.voterAccount.address.$invalid }"
              v-model="$v.voterAccount.address.$model"
              required
            />
            <div v-if="$v.voterAccount.address.$anyDirty && $v.voterAccount.address.$invalid">
              <small class="form-text text-danger" v-if="!$v.voterAccount.address.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voterAccount.voter')" for="voter-account-voter">Voter</label>
            <select class="form-control" id="voter-account-voter" data-cy="voter" name="voter" v-model="voterAccount.voter">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="voterAccount.voter && voterOption.id === voterAccount.voter.id ? voterAccount.voter : voterOption"
                v-for="voterOption in voters"
                :key="voterOption.id"
              >
                {{ voterOption.id }}
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
            :disabled="$v.voterAccount.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./voter-account-update.component.ts"></script>
