<template>
  <div>
    <h2 id="page-heading" data-cy="ManagedAccountHeading">
      <span v-text="$t('upraizApp.managedAccount.home.title')" id="managed-account-heading">Managed Accounts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.managedAccount.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ManagedAccountCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-managed-account"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.managedAccount.home.createLabel')"> Create a new Managed Account </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && managedAccounts && managedAccounts.length === 0">
      <span v-text="$t('upraizApp.managedAccount.home.notFound')">No managedAccounts found</span>
    </div>
    <div class="table-responsive" v-if="managedAccounts && managedAccounts.length > 0">
      <table class="table table-striped" aria-describedby="managedAccounts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.managedAccount.accountName')">Account Name</span></th>
            <th scope="row"><span v-text="$t('upraizApp.managedAccount.ccy')">Ccy</span></th>
            <th scope="row"><span v-text="$t('upraizApp.managedAccount.address')">Address</span></th>
            <th scope="row"><span v-text="$t('upraizApp.managedAccount.seed')">Seed</span></th>
            <th scope="row"><span v-text="$t('upraizApp.managedAccount.voteManager')">Vote Manager</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="managedAccount in managedAccounts" :key="managedAccount.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ManagedAccountView', params: { managedAccountId: managedAccount.id } }">{{
                managedAccount.id
              }}</router-link>
            </td>
            <td>{{ managedAccount.accountName }}</td>
            <td v-text="$t('upraizApp.VoteCcy.' + managedAccount.ccy)">{{ managedAccount.ccy }}</td>
            <td>{{ managedAccount.address }}</td>
            <td>{{ managedAccount.seed }}</td>
            <td>
              <div v-if="managedAccount.voteManager">
                <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: managedAccount.voteManager.id } }">{{
                  managedAccount.voteManager.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ManagedAccountView', params: { managedAccountId: managedAccount.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ManagedAccountEdit', params: { managedAccountId: managedAccount.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(managedAccount)"
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
        ><span id="upraizApp.managedAccount.delete.question" data-cy="managedAccountDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-managedAccount-heading" v-text="$t('upraizApp.managedAccount.delete.question', { id: removeId })">
          Are you sure you want to delete this Managed Account?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-managedAccount"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeManagedAccount()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./managed-account.component.ts"></script>
