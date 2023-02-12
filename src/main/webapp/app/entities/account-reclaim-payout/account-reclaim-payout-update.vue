<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.accountReclaimPayout.home.createOrEditLabel"
          data-cy="AccountReclaimPayoutCreateUpdateHeading"
          v-text="$t('upraizApp.accountReclaimPayout.home.createOrEditLabel')"
        >
          Create or edit a AccountReclaimPayout
        </h2>
        <div>
          <div class="form-group" v-if="accountReclaimPayout.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="accountReclaimPayout.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimPayout.amount')" for="account-reclaim-payout-amount"
              >Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="amount"
              id="account-reclaim-payout-amount"
              data-cy="amount"
              :class="{ valid: !$v.accountReclaimPayout.amount.$invalid, invalid: $v.accountReclaimPayout.amount.$invalid }"
              v-model.number="$v.accountReclaimPayout.amount.$model"
              required
            />
            <div v-if="$v.accountReclaimPayout.amount.$anyDirty && $v.accountReclaimPayout.amount.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimPayout.amount.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.accountReclaimPayout.amount.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimPayout.timestamp')" for="account-reclaim-payout-timestamp"
              >Timestamp</label
            >
            <div class="d-flex">
              <input
                id="account-reclaim-payout-timestamp"
                data-cy="timestamp"
                type="datetime-local"
                class="form-control"
                name="timestamp"
                :class="{ valid: !$v.accountReclaimPayout.timestamp.$invalid, invalid: $v.accountReclaimPayout.timestamp.$invalid }"
                required
                :value="convertDateTimeFromServer($v.accountReclaimPayout.timestamp.$model)"
                @change="updateZonedDateTimeField('timestamp', $event)"
              />
            </div>
            <div v-if="$v.accountReclaimPayout.timestamp.$anyDirty && $v.accountReclaimPayout.timestamp.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimPayout.timestamp.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimPayout.timestamp.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimPayout.ccy')" for="account-reclaim-payout-ccy">Ccy</label>
            <select
              class="form-control"
              name="ccy"
              :class="{ valid: !$v.accountReclaimPayout.ccy.$invalid, invalid: $v.accountReclaimPayout.ccy.$invalid }"
              v-model="$v.accountReclaimPayout.ccy.$model"
              id="account-reclaim-payout-ccy"
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
            <div v-if="$v.accountReclaimPayout.ccy.$anyDirty && $v.accountReclaimPayout.ccy.$invalid">
              <small class="form-text text-danger" v-if="!$v.accountReclaimPayout.ccy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.accountReclaimPayout.txnRef')" for="account-reclaim-payout-txnRef"
              >Txn Ref</label
            >
            <input
              type="text"
              class="form-control"
              name="txnRef"
              id="account-reclaim-payout-txnRef"
              data-cy="txnRef"
              :class="{ valid: !$v.accountReclaimPayout.txnRef.$invalid, invalid: $v.accountReclaimPayout.txnRef.$invalid }"
              v-model="$v.accountReclaimPayout.txnRef.$model"
              required
            />
            <div v-if="$v.accountReclaimPayout.txnRef.$anyDirty && $v.accountReclaimPayout.txnRef.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.accountReclaimPayout.txnRef.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('upraizApp.accountReclaimPayout.managedAccount')"
              for="account-reclaim-payout-managedAccount"
              >Managed Account</label
            >
            <select
              class="form-control"
              id="account-reclaim-payout-managedAccount"
              data-cy="managedAccount"
              name="managedAccount"
              v-model="accountReclaimPayout.managedAccount"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  accountReclaimPayout.managedAccount && managedAccountOption.id === accountReclaimPayout.managedAccount.id
                    ? accountReclaimPayout.managedAccount
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
              v-text="$t('upraizApp.accountReclaimPayout.voteManager')"
              for="account-reclaim-payout-voteManager"
              >Vote Manager</label
            >
            <select
              class="form-control"
              id="account-reclaim-payout-voteManager"
              data-cy="voteManager"
              name="voteManager"
              v-model="accountReclaimPayout.voteManager"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  accountReclaimPayout.voteManager && voteManagerOption.id === accountReclaimPayout.voteManager.id
                    ? accountReclaimPayout.voteManager
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
            :disabled="$v.accountReclaimPayout.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./account-reclaim-payout-update.component.ts"></script>
