<template>
  <div>
    <h2 id="page-heading" data-cy="VoteManagerHeading">
      <span v-text="$t('upraizApp.voteManager.home.title')" id="vote-manager-heading">Vote Managers</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.voteManager.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VoteManagerCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-vote-manager"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.voteManager.home.createLabel')"> Create a new Vote Manager </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && voteManagers && voteManagers.length === 0">
      <span v-text="$t('upraizApp.voteManager.home.notFound')">No voteManagers found</span>
    </div>
    <div class="table-responsive" v-if="voteManagers && voteManagers.length > 0">
      <table class="table table-striped" aria-describedby="voteManagers">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.email')">Email</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.name')">Name</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.created')">Created</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.active')">Active</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.comment')">Comment</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voteManager.voteManagerPreferences')">Vote Manager Preferences</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="voteManager in voteManagers" :key="voteManager.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: voteManager.id } }">{{ voteManager.id }}</router-link>
            </td>
            <td>{{ voteManager.email }}</td>
            <td>{{ voteManager.name }}</td>
            <td>{{ voteManager.created ? $d(Date.parse(voteManager.created), 'short') : '' }}</td>
            <td>{{ voteManager.active }}</td>
            <td>{{ voteManager.comment }}</td>
            <td>
              <div v-if="voteManager.voteManagerPreferences">
                <router-link
                  :to="{ name: 'VoteManagerPreferencesView', params: { voteManagerPreferencesId: voteManager.voteManagerPreferences.id } }"
                  >{{ voteManager.voteManagerPreferences.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: voteManager.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VoteManagerEdit', params: { voteManagerId: voteManager.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(voteManager)"
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
        ><span id="upraizApp.voteManager.delete.question" data-cy="voteManagerDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-voteManager-heading" v-text="$t('upraizApp.voteManager.delete.question', { id: removeId })">
          Are you sure you want to delete this Vote Manager?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-voteManager"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVoteManager()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./vote-manager.component.ts"></script>
