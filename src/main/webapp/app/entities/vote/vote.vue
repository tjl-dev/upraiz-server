<template>
  <div>
    <h2 id="page-heading" data-cy="VoteHeading">
      <span v-text="$t('upraizApp.vote.home.title')" id="vote-heading">Votes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.vote.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VoteCreate' }" custom v-slot="{ navigate }">
          <button @click="navigate" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-vote">
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.vote.home.createLabel')"> Create a new Vote </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && votes && votes.length === 0">
      <span v-text="$t('upraizApp.vote.home.notFound')">No votes found</span>
    </div>
    <div class="table-responsive" v-if="votes && votes.length > 0">
      <table class="table table-striped" aria-describedby="votes">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.votedTimestamp')">Voted Timestamp</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.verified')">Verified</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.verifiedTime')">Verified Time</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.verifiedBy')">Verified By</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.paid')">Paid</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.votePayout')">Vote Payout</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.voteTarget')">Vote Target</span></th>
            <th scope="row"><span v-text="$t('upraizApp.vote.voter')">Voter</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vote in votes" :key="vote.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VoteView', params: { voteId: vote.id } }">{{ vote.id }}</router-link>
            </td>
            <td>{{ vote.votedTimestamp ? $d(Date.parse(vote.votedTimestamp), 'short') : '' }}</td>
            <td>{{ vote.verified }}</td>
            <td>{{ vote.verifiedTime ? $d(Date.parse(vote.verifiedTime), 'short') : '' }}</td>
            <td>{{ vote.verifiedBy }}</td>
            <td>{{ vote.paid }}</td>
            <td>
              <div v-if="vote.votePayout">
                <router-link :to="{ name: 'VotePayoutView', params: { votePayoutId: vote.votePayout.id } }">{{
                  vote.votePayout.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="vote.voteTarget">
                <router-link :to="{ name: 'VoteTargetView', params: { voteTargetId: vote.voteTarget.id } }">{{
                  vote.voteTarget.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="vote.voter">
                <router-link :to="{ name: 'VoterView', params: { voterId: vote.voter.id } }">{{ vote.voter.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VoteView', params: { voteId: vote.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VoteEdit', params: { voteId: vote.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(vote)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="upraizApp.vote.delete.question" data-cy="voteDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-vote-heading" v-text="$t('upraizApp.vote.delete.question', { id: removeId })">
          Are you sure you want to delete this Vote?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-vote"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVote()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./vote.component.ts"></script>
