<template>
  <div>
    <h2 id="page-heading" data-cy="VotePayoutHeading">
      <span v-text="$t('upraizApp.votePayout.home.title')" id="vote-payout-heading">Vote Payouts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.votePayout.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VotePayoutCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-vote-payout"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.votePayout.home.createLabel')"> Create a new Vote Payout </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && votePayouts && votePayouts.length === 0">
      <span v-text="$t('upraizApp.votePayout.home.notFound')">No votePayouts found</span>
    </div>
    <div class="table-responsive" v-if="votePayouts && votePayouts.length > 0">
      <table class="table table-striped" aria-describedby="votePayouts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.votePayout.paidTime')">Paid Time</span></th>
            <th scope="row"><span v-text="$t('upraizApp.votePayout.paidAmount')">Paid Amount</span></th>
            <th scope="row"><span v-text="$t('upraizApp.votePayout.paidCcy')">Paid Ccy</span></th>
            <th scope="row"><span v-text="$t('upraizApp.votePayout.voterAccount')">Voter Account</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="votePayout in votePayouts" :key="votePayout.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VotePayoutView', params: { votePayoutId: votePayout.id } }">{{ votePayout.id }}</router-link>
            </td>
            <td>{{ votePayout.paidTime ? $d(Date.parse(votePayout.paidTime), 'short') : '' }}</td>
            <td>{{ votePayout.paidAmount }}</td>
            <td v-text="$t('upraizApp.VoteCcy.' + votePayout.paidCcy)">{{ votePayout.paidCcy }}</td>
            <td>
              <div v-if="votePayout.voterAccount">
                <router-link :to="{ name: 'VoterAccountView', params: { voterAccountId: votePayout.voterAccount.id } }">{{
                  votePayout.voterAccount.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VotePayoutView', params: { votePayoutId: votePayout.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VotePayoutEdit', params: { votePayoutId: votePayout.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(votePayout)"
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
        ><span id="upraizApp.votePayout.delete.question" data-cy="votePayoutDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-votePayout-heading" v-text="$t('upraizApp.votePayout.delete.question', { id: removeId })">
          Are you sure you want to delete this Vote Payout?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-votePayout"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVotePayout()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./vote-payout.component.ts"></script>
