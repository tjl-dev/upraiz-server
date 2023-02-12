<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.accountReclaimRequest.home.createOrEditLabel"
          data-cy="AccountReclaimRequestCreateUpdateHeading"
          v-text="$t('upraizApp.accountReclaimRequest.home.createOrEditLabel')"
        >
          Create or edit a AccountReclaimRequest
        </h2>
        <div>
          <div class="form-group" v-if="accountReclaimRequest.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="accountReclaimRequest.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimRequest.amount')" for="account-reclaim-request-amount"
              >Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="amount"
              id="account-reclaim-request-amount"
              data-cy="amount"
              :class="{ valid: !$v.accountReclaimRequest.amount.$invalid, invalid: $v.accountReclaimRequest.amount.$invalid }"
              v-model.number="$v.accountReclaimRequest.amount.$model"
              required
            />
            <div v-if="$v.accountReclaimRequest.amount.$anyDirty && $v.accountReclaimRequest.amount.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimRequest.amount.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.accountReclaimRequest.amount.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.accountReclaimRequest.timestamp')"
              for="account-reclaim-request-timestamp"
              >Timestamp</label
            >
            <div class="d-flex">
              <input
                id="account-reclaim-request-timestamp"
                data-cy="timestamp"
                type="datetime-local"
                class="form-control"
                name="timestamp"
                :class="{ valid: !$v.accountReclaimRequest.timestamp.$invalid, invalid: $v.accountReclaimRequest.timestamp.$invalid }"
                required
                :value="convertDateTimeFromServer($v.accountReclaimRequest.timestamp.$model)"
                @change="updateZonedDateTimeField('timestamp', $event)"
              />
            </div>
            <div v-if="$v.accountReclaimRequest.timestamp.$anyDirty && $v.accountReclaimRequest.timestamp.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimRequest.timestamp.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimRequest.timestamp.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimRequest.ccy')" for="account-reclaim-request-ccy"
              >Ccy</label
            >
            <select
              class="form-control"
              name="ccy"
              :class="{ valid: !$v.accountReclaimRequest.ccy.$invalid, invalid: $v.accountReclaimRequest.ccy.$invalid }"
              v-model="$v.accountReclaimRequest.ccy.$model"
              id="account-reclaim-request-ccy"
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
            <div v-if="$v.accountReclaimRequest.ccy.$anyDirty && $v.accountReclaimRequest.ccy.$invalid">
              <small class="form-text text-danger" v-if="!$v.accountReclaimRequest.ccy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimRequest.active')" for="account-reclaim-request-active"
              >Active</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="account-reclaim-request-active"
              data-cy="active"
              :class="{ valid: !$v.accountReclaimRequest.active.$invalid, invalid: $v.accountReclaimRequest.active.$invalid }"
              v-model="$v.accountReclaimRequest.active.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.accountReclaimRequest.accountReclaimPayout')"
              for="account-reclaim-request-accountReclaimPayout"
              >Account Reclaim Payout</label
            >
            <select
              class="form-control"
              id="account-reclaim-request-accountReclaimPayout"
              data-cy="accountReclaimPayout"
              name="accountReclaimPayout"
              v-model="accountReclaimRequest.accountReclaimPayout"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  accountReclaimRequest.accountReclaimPayout &&
                  accountReclaimPayoutOption.id === accountReclaimRequest.accountReclaimPayout.id
                    ? accountReclaimRequest.accountReclaimPayout
                    : accountReclaimPayoutOption
                "
                v-for="accountReclaimPayoutOption in accountReclaimPayouts"
                :key="accountReclaimPayoutOption.id"
              >
                {{ accountReclaimPayoutOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.accountReclaimRequest.managedAccount')"
              for="account-reclaim-request-managedAccount"
              >Managed Account</label
            >
            <select
              class="form-control"
              id="account-reclaim-request-managedAccount"
              data-cy="managedAccount"
              name="managedAccount"
              v-model="accountReclaimRequest.managedAccount"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  accountReclaimRequest.managedAccount && managedAccountOption.id === accountReclaimRequest.managedAccount.id
                    ? accountReclaimRequest.managedAccount
                    : managedAccountOption
                "
                v-for="managedAccountOption in managedAccounts"
                :key="managedAccountOption.id"
              >
                {{ managedAccountOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.accountReclaimRequest.voteManager')"
              for="account-reclaim-request-voteManager"
              >Vote Manager</label
            >
            <select
              class="form-control"
              id="account-reclaim-request-voteManager"
              data-cy="voteManager"
              name="voteManager"
              v-model="accountReclaimRequest.voteManager"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  accountReclaimRequest.voteManager && voteManagerOption.id === accountReclaimRequest.voteManager.id
                    ? accountReclaimRequest.voteManager
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
            :disabled="$v.accountReclaimRequest.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./account-reclaim-request-update.component.ts"></script>
