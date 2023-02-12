<template>
  <div>
    <h2 id="page-heading" data-cy="VoterPreferencesHeading">
      <span v-text="$t('upraizApp.voterPreferences.home.title')" id="voter-preferences-heading">Voter Preferences</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.voterPreferences.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'VoterPreferencesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-voter-preferences"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.voterPreferences.home.createLabel')"> Create a new Voter Preferences </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && voterPreferences && voterPreferences.length === 0">
      <span v-text="$t('upraizApp.voterPreferences.home.notFound')">No voterPreferences found</span>
    </div>
    <div class="table-responsive" v-if="voterPreferences && voterPreferences.length > 0">
      <table class="table table-striped" aria-describedby="voterPreferences">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.voterPreferences.receiveCcy')">Receive Ccy</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="voterPreferences in voterPreferences" :key="voterPreferences.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'VoterPreferencesView', params: { voterPreferencesId: voterPreferences.id } }">{{
                voterPreferences.id
              }}</router-link>
            </td>
            <td v-text="$t('upraizApp.VoteCcy.' + voterPreferences.receiveCcy)">{{ voterPreferences.receiveCcy }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'VoterPreferencesView', params: { voterPreferencesId: voterPreferences.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'VoterPreferencesEdit', params: { voterPreferencesId: voterPreferences.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(voterPreferences)"
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
        ><span
          id="upraizApp.voterPreferences.delete.question"
          data-cy="voterPreferencesDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-voterPreferences-heading" v-text="$t('upraizApp.voterPreferences.delete.question', { id: removeId })">
          Are you sure you want to delete this Voter Preferences?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-voterPreferences"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeVoterPreferences()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./voter-preferences.component.ts"></script>
