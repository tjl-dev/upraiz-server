<template>
  <div>
    <h2 id="page-heading" data-cy="AccountReclaimRequestHeading">
      <span v-text="$t('upraizApp.accountReclaimRequest.home.title')" id="account-reclaim-request-heading">Account Reclaim Requests</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.accountReclaimRequest.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AccountReclaimRequestCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-account-reclaim-request"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.accountReclaimRequest.home.createLabel')"> Create a new Account Reclaim Request </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && accountReclaimRequests && accountReclaimRequests.length === 0">
      <span v-text="$t('upraizApp.accountReclaimRequest.home.notFound')">No accountReclaimRequests found</span>
    </div>
    <div class="table-responsive" v-if="accountReclaimRequests && accountReclaimRequests.length > 0">
      <table class="table table-striped" aria-describedby="accountReclaimRequests">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.amount')">Amount</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.timestamp')">Timestamp</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.ccy')">Ccy</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.active')">Active</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.accountReclaimPayout')">Account Reclaim Payout</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.managedAccount')">Managed Account</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimRequest.voteManager')">Vote Manager</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="accountReclaimRequest in accountReclaimRequests" :key="accountReclaimRequest.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AccountReclaimRequestView', params: { accountReclaimRequestId: accountReclaimRequest.id } }">{{
                accountReclaimRequest.id
              }}</router-link>
            </td>
            <td>{{ accountReclaimRequest.amount }}</td>
            <td>{{ accountReclaimRequest.timestamp ? $d(Date.parse(accountReclaimRequest.timestamp), 'short') : '' }}</td>
            <td v-text="$t('upraizApp.VoteCcy.' + accountReclaimRequest.ccy)">{{ accountReclaimRequest.ccy }}</td>
            <td>{{ accountReclaimRequest.active }}</td>
            <td>
              <div v-if="accountReclaimRequest.accountReclaimPayout">
                <router-link
                  :to="{
                    name: 'AccountReclaimPayoutView',
                    params: { accountReclaimPayoutId: accountReclaimRequest.accountReclaimPayout.id },
                  }"
                  >{{ accountReclaimRequest.accountReclaimPayout.id }}</router-link
                >
              </div>
            </td>
            <td>
              <div v-if="accountReclaimRequest.managedAccount">
                <router-link :to="{ name: 'ManagedAccountView', params: { managedAccountId: accountReclaimRequest.managedAccount.id } }">{{
                  accountReclaimRequest.managedAccount.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="accountReclaimRequest.voteManager">
                <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: accountReclaimRequest.voteManager.id } }">{{
                  accountReclaimRequest.voteManager.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AccountReclaimRequestView', params: { accountReclaimRequestId: accountReclaimRequest.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'AccountReclaimRequestEdit', params: { accountReclaimRequestId: accountReclaimRequest.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(accountReclaimRequest)"
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
          id="upraizApp.accountReclaimRequest.delete.question"
          data-cy="accountReclaimRequestDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-accountReclaimRequest-heading" v-text="$t('upraizApp.accountReclaimRequest.delete.question', { id: removeId })">
          Are you sure you want to delete this Account Reclaim Request?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-accountReclaimRequest"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAccountReclaimRequest()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./account-reclaim-request.component.ts"></script>
