<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.votePayout.home.createOrEditLabel"
          data-cy="VotePayoutCreateUpdateHeading"
          v-text="$t('upraizApp.votePayout.home.createOrEditLabel')"
        >
          Create or edit a VotePayout
        </h2>
        <div>
          <div class="form-group" v-if="votePayout.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="votePayout.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.votePayout.paidTime')" for="vote-payout-paidTime">Paid Time</label>
            <div class="d-flex">
              <input
                id="vote-payout-paidTime"
                data-cy="paidTime"
                type="datetime-local"
                class="form-control"
                name="paidTime"
                :class="{ valid: !$v.votePayout.paidTime.$invalid, invalid: $v.votePayout.paidTime.$invalid }"
                required
                :value="convertDateTimeFromServer($v.votePayout.paidTime.$model)"
                @change="updateZonedDateTimeField('paidTime', $event)"
              />
            </div>
            <div v-if="$v.votePayout.paidTime.$anyDirty && $v.votePayout.paidTime.$invalid">
              <small class="form-text text-danger" v-if="!$v.votePayout.paidTime.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.votePayout.paidTime.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.votePayout.paidAmount')" for="vote-payout-paidAmount"
              >Paid Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="paidAmount"
              id="vote-payout-paidAmount"
              data-cy="paidAmount"
              :class="{ valid: !$v.votePayout.paidAmount.$invalid, invalid: $v.votePayout.paidAmount.$invalid }"
              v-model.number="$v.votePayout.paidAmount.$model"
              required
            />
            <div v-if="$v.votePayout.paidAmount.$anyDirty && $v.votePayout.paidAmount.$invalid">
              <small class="form-text text-danger" v-if="!$v.votePayout.paidAmount.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.votePayout.paidAmount.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.votePayout.paidCcy')" for="vote-payout-paidCcy">Paid Ccy</label>
            <select
              class="form-control"
              name="paidCcy"
              :class="{ valid: !$v.votePayout.paidCcy.$invalid, invalid: $v.votePayout.paidCcy.$invalid }"
              v-model="$v.votePayout.paidCcy.$model"
              id="vote-payout-paidCcy"
              data-cy="paidCcy"
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
            <div v-if="$v.votePayout.paidCcy.$anyDirty && $v.votePayout.paidCcy.$invalid">
              <small class="form-text text-danger" v-if="!$v.votePayout.paidCcy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.votePayout.voterAccount')" for="vote-payout-voterAccount"
              >Voter Account</label
            >
            <select
              class="form-control"
              id="vote-payout-voterAccount"
              data-cy="voterAccount"
              name="voterAccount"
              v-model="votePayout.voterAccount"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  votePayout.voterAccount && voterAccountOption.id === votePayout.voterAccount.id
                    ? votePayout.voterAccount
                    : voterAccountOption
                "
                v-for="voterAccountOption in voterAccounts"
                :key="voterAccountOption.id"
              >
                {{ voterAccountOption.id }}
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
            :disabled="$v.votePayout.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./vote-payout-update.component.ts"></script>
