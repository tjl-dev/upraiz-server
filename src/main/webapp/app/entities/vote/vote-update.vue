<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.vote.home.createOrEditLabel"
          data-cy="VoteCreateUpdateHeading"
          v-text="$t('upraizApp.vote.home.createOrEditLabel')"
        >
          Create or edit a Vote
        </h2>
        <div>
          <div class="form-group" v-if="vote.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="vote.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.votedTimestamp')" for="vote-votedTimestamp">Voted Timestamp</label>
            <div class="d-flex">
              <input
                id="vote-votedTimestamp"
                data-cy="votedTimestamp"
                type="datetime-local"
                class="form-control"
                name="votedTimestamp"
                :class="{ valid: !$v.vote.votedTimestamp.$invalid, invalid: $v.vote.votedTimestamp.$invalid }"
                required
                :value="convertDateTimeFromServer($v.vote.votedTimestamp.$model)"
                @change="updateZonedDateTimeField('votedTimestamp', $event)"
              />
            </div>
            <div v-if="$v.vote.votedTimestamp.$anyDirty && $v.vote.votedTimestamp.$invalid">
              <small class="form-text text-danger" v-if="!$v.vote.votedTimestamp.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.vote.votedTimestamp.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.verified')" for="vote-verified">Verified</label>
            <input
              type="checkbox"
              class="form-check"
              name="verified"
              id="vote-verified"
              data-cy="verified"
              :class="{ valid: !$v.vote.verified.$invalid, invalid: $v.vote.verified.$invalid }"
              v-model="$v.vote.verified.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.verifiedTime')" for="vote-verifiedTime">Verified Time</label>
            <div class="d-flex">
              <input
                id="vote-verifiedTime"
                data-cy="verifiedTime"
                type="datetime-local"
                class="form-control"
                name="verifiedTime"
                :class="{ valid: !$v.vote.verifiedTime.$invalid, invalid: $v.vote.verifiedTime.$invalid }"
                :value="convertDateTimeFromServer($v.vote.verifiedTime.$model)"
                @change="updateZonedDateTimeField('verifiedTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.verifiedBy')" for="vote-verifiedBy">Verified By</label>
            <input
              type="text"
              class="form-control"
              name="verifiedBy"
              id="vote-verifiedBy"
              data-cy="verifiedBy"
              :class="{ valid: !$v.vote.verifiedBy.$invalid, invalid: $v.vote.verifiedBy.$invalid }"
              v-model="$v.vote.verifiedBy.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.paid')" for="vote-paid">Paid</label>
            <input
              type="checkbox"
              class="form-check"
              name="paid"
              id="vote-paid"
              data-cy="paid"
              :class="{ valid: !$v.vote.paid.$invalid, invalid: $v.vote.paid.$invalid }"
              v-model="$v.vote.paid.$model"
              required
            />
            <div v-if="$v.vote.paid.$anyDirty && $v.vote.paid.$invalid">
              <small class="form-text text-danger" v-if="!$v.vote.paid.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.voteTarget')" for="vote-voteTarget">Vote Target</label>
            <select class="form-control" id="vote-voteTarget" data-cy="voteTarget" name="voteTarget" v-model="vote.voteTarget">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="vote.voteTarget && voteTargetOption.id === vote.voteTarget.id ? vote.voteTarget : voteTargetOption"
                v-for="voteTargetOption in voteTargets"
                :key="voteTargetOption.id"
              >
                {{ voteTargetOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.votePayout')" for="vote-votePayout">Vote Payout</label>
            <select class="form-control" id="vote-votePayout" data-cy="votePayout" name="votePayout" v-model="vote.votePayout">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="vote.votePayout && votePayoutOption.id === vote.votePayout.id ? vote.votePayout : votePayoutOption"
                v-for="votePayoutOption in votePayouts"
                :key="votePayoutOption.id"
              >
                {{ votePayoutOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.vote.voter')" for="vote-voter">Voter</label>
            <select class="form-control" id="vote-voter" data-cy="voter" name="voter" v-model="vote.voter">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="vote.voter && voterOption.id === vote.voter.id ? vote.voter : voterOption"
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
            :disabled="$v.vote.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./vote-update.component.ts"></script>
