<template>
  <div>
    <h2 id="page-heading" data-cy="VoterAccountHeading">
      <span v-text="$t('upraizApp.voterAccount.home.title')" id="voter-account-heading">Voter Accounts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.voterAccount.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VoterAccountCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-voter-account"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.voterAccount.home.createLabel')"> Create a new Voter Account </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && voterAccounts && voterAccounts.length === 0">
      <span v-text="$t('upraizApp.voterAccount.home.notFound')">No voterAccounts found</span>
    </div>
    <div class="table-responsive" v-if="voterAccounts && voterAccounts.length > 0">
      <table class="table table-striped" aria-describedby="voterAccounts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voterAccount.accountname')">Accountname</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voterAccount.ccy')">Ccy</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voterAccount.address')">Address</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voterAccount.voter')">Voter</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="voterAccount in voterAccounts" :key="voterAccount.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VoterAccountView', params: { voterAccountId: voterAccount.id } }">{{
                voterAccount.id
              }}</router-link>
            </td>
            <td>{{ voterAccount.accountname }}</td>
            <td v-text="$t('upraizApp.VoteCcy.' + voterAccount.ccy)">{{ voterAccount.ccy }}</td>
            <td>{{ voterAccount.address }}</td>
            <td>
              <div v-if="voterAccount.voter">
                <router-link :to="{ name: 'VoterView', params: { voterId: voterAccount.voter.id } }">{{
                  voterAccount.voter.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VoterAccountView', params: { voterAccountId: voterAccount.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VoterAccountEdit', params: { voterAccountId: voterAccount.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(voterAccount)"
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
        ><span id="upraizApp.voterAccount.delete.question" data-cy="voterAccountDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-voterAccount-heading" v-text="$t('upraizApp.voterAccount.delete.question', { id: removeId })">
          Are you sure you want to delete this Voter Account?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-voterAccount"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVoterAccount()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./voter-account.component.ts"></script>
