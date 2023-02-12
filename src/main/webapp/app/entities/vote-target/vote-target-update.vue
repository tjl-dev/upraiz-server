<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="upraizApp.voteTarget.home.createOrEditLabel"
          data-cy="VoteTargetCreateUpdateHeading"
          v-text="$t('upraizApp.voteTarget.home.createOrEditLabel')"
        >
          Create or edit a VoteTarget
        </h2>
        <div>
          <div class="form-group" v-if="voteTarget.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="voteTarget.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.url')" for="vote-target-url">Url</label>
            <input
              type="text"
              class="form-control"
              name="url"
              id="vote-target-url"
              data-cy="url"
              :class="{ valid: !$v.voteTarget.url.$invalid, invalid: $v.voteTarget.url.$invalid }"
              v-model="$v.voteTarget.url.$model"
              required
            />
            <div v-if="$v.voteTarget.url.$anyDirty && $v.voteTarget.url.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteTarget.url.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.votetype')" for="vote-target-votetype">Votetype</label>
            <select
              class="form-control"
              name="votetype"
              :class="{ valid: !$v.voteTarget.votetype.$invalid, invalid: $v.voteTarget.votetype.$invalid }"
              v-model="$v.voteTarget.votetype.$model"
              id="vote-target-votetype"
              data-cy="votetype"
              required
            >
              <option
                v-for="voteTargetType in voteTargetTypeValues"
                :key="voteTargetType"
                v-bind:value="voteTargetType"
                v-bind:label="$t('upraizApp.VoteTargetType.' + voteTargetType)"
              >
                {{ voteTargetType }}
              </option>
            </select>
            <div v-if="$v.voteTarget.votetype.$anyDirty && $v.voteTarget.votetype.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteTarget.votetype.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.payout')" for="vote-target-payout">Payout</label>
            <input
              type="number"
              class="form-control"
              name="payout"
              id="vote-target-payout"
              data-cy="payout"
              :class="{ valid: !$v.voteTarget.payout.$invalid, invalid: $v.voteTarget.payout.$invalid }"
              v-model.number="$v.voteTarget.payout.$model"
              required
            />
            <div v-if="$v.voteTarget.payout.$anyDirty && $v.voteTarget.payout.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteTarget.payout.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.voteTarget.payout.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.ccy')" for="vote-target-ccy">Ccy</label>
            <select
              class="form-control"
              name="ccy"
              :class="{ valid: !$v.voteTarget.ccy.$invalid, invalid: $v.voteTarget.ccy.$invalid }"
              v-model="$v.voteTarget.ccy.$model"
              id="vote-target-ccy"
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
            <div v-if="$v.voteTarget.ccy.$anyDirty && $v.voteTarget.ccy.$invalid">
              <small class="form-text text-danger" v-if="!$v.voteTarget.ccy.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.comment')" for="vote-target-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="vote-target-comment"
              data-cy="comment"
              :class="{ valid: !$v.voteTarget.comment.$invalid, invalid: $v.voteTarget.comment.$invalid }"
              v-model="$v.voteTarget.comment.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.active')" for="vote-target-active">Active</label>
            <input
              type="checkbox"
              class="form-check"
              name="active"
              id="vote-target-active"
              data-cy="active"
              :class="{ valid: !$v.voteTarget.active.$invalid, invalid: $v.voteTarget.active.$invalid }"
              v-model="$v.voteTarget.active.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.funded')" for="vote-target-funded">Funded</label>
            <input
              type="checkbox"
              class="form-check"
              name="funded"
              id="vote-target-funded"
              data-cy="funded"
              :class="{ valid: !$v.voteTarget.funded.$invalid, invalid: $v.voteTarget.funded.$invalid }"
              v-model="$v.voteTarget.funded.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.created')" for="vote-target-created">Created</label>
            <div class="d-flex">
              <input
                id="vote-target-created"
                data-cy="created"
                type="datetime-local"
                class="form-control"
                name="created"
                :class="{ valid: !$v.voteTarget.created.$invalid, invalid: $v.voteTarget.created.$invalid }"
                :value="convertDateTimeFromServer($v.voteTarget.created.$model)"
                @change="updateZonedDateTimeField('created', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.expiry')" for="vote-target-expiry">Expiry</label>
            <div class="d-flex">
              <input
                id="vote-target-expiry"
                data-cy="expiry"
                type="datetime-local"
                class="form-control"
                name="expiry"
                :class="{ valid: !$v.voteTarget.expiry.$invalid, invalid: $v.voteTarget.expiry.$invalid }"
                :value="convertDateTimeFromServer($v.voteTarget.expiry.$model)"
                @change="updateZonedDateTimeField('expiry', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.boosted')" for="vote-target-boosted">Boosted</label>
            <input
              type="checkbox"
              class="form-check"
              name="boosted"
              id="vote-target-boosted"
              data-cy="boosted"
              :class="{ valid: !$v.voteTarget.boosted.$invalid, invalid: $v.voteTarget.boosted.$invalid }"
              v-model="$v.voteTarget.boosted.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('upraizApp.voteTarget.voteManager')" for="vote-target-voteManager"
              >Vote Manager</label
            >
            <select
              class="form-control"
              id="vote-target-voteManager"
              data-cy="voteManager"
              name="voteManager"
              v-model="voteTarget.voteManager"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  voteTarget.voteManager && voteManagerOption.id === voteTarget.voteManager.id ? voteTarget.voteManager : voteManagerOption
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
            :disabled="$v.voteTarget.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./vote-target-update.component.ts"></script>
