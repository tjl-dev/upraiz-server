<template>
  <div>
    <h2 id="page-heading" data-cy="VoterHeading">
      <span v-text="$t('upraizApp.voter.home.title')" id="voter-heading">Voters</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.voter.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VoterCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-voter"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.voter.home.createLabel')"> Create a new Voter </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && voters && voters.length === 0">
      <span v-text="$t('upraizApp.voter.home.notFound')">No voters found</span>
    </div>
    <div class="table-responsive" v-if="voters && voters.length > 0">
      <table class="table table-striped" aria-describedby="voters">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.email')">Email</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.name')">Name</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.created')">Created</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.active')">Active</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.comment')">Comment</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voter.voterPreferences')">Voter Preferences</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="voter in voters" :key="voter.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VoterView', params: { voterId: voter.id } }">{{ voter.id }}</router-link>
            </td>
            <td>{{ voter.email }}</td>
            <td>{{ voter.name }}</td>
            <td>{{ voter.created ? $d(Date.parse(voter.created), 'short') : '' }}</td>
            <td>{{ voter.active }}</td>
            <td>{{ voter.comment }}</td>
            <td>
              <div v-if="voter.voterPreferences">
                <router-link :to="{ name: 'VoterPreferencesView', params: { voterPreferencesId: voter.voterPreferences.id } }">{{
                  voter.voterPreferences.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'VoterView', params: { voterId: voter.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'VoterEdit', params: { voterId: voter.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(voter)"
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
        ><span id="upraizApp.voter.delete.question" data-cy="voterDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-voter-heading" v-text="$t('upraizApp.voter.delete.question', { id: removeId })">
          Are you sure you want to delete this Voter?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-voter"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVoter()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./voter.component.ts"></script>
