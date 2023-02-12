<template>
  <div>
    <h2 id="page-heading" data-cy="AccountReclaimPayoutHeading">
      <span v-text="$t('upraizApp.accountReclaimPayout.home.title')" id="account-reclaim-payout-heading">Account Reclaim Payouts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('upraizApp.accountReclaimPayout.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AccountReclaimPayoutCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-account-reclaim-payout"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('upraizApp.accountReclaimPayout.home.createLabel')"> Create a new Account Reclaim Payout </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && accountReclaimPayouts && accountReclaimPayouts.length === 0">
      <span v-text="$t('upraizApp.accountReclaimPayout.home.notFound')">No accountReclaimPayouts found</span>
    </div>
    <div class="table-responsive" v-if="accountReclaimPayouts && accountReclaimPayouts.length > 0">
      <table class="table table-striped" aria-describedby="accountReclaimPayouts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.amount')">Amount</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.timestamp')">Timestamp</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.ccy')">Ccy</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.txnRef')">Txn Ref</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.managedAccount')">Managed Account</span></th>
            <th scope="row"><span v-text="$t('upraizApp.accountReclaimPayout.voteManager')">Vote Manager</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="accountReclaimPayout in accountReclaimPayouts" :key="accountReclaimPayout.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AccountReclaimPayoutView', params: { accountReclaimPayoutId: accountReclaimPayout.id } }">{{
                accountReclaimPayout.id
              }}</router-link>
            </td>
            <td>{{ accountReclaimPayout.amount }}</td>
            <td>{{ accountReclaimPayout.timestamp ? $d(Date.parse(accountReclaimPayout.timestamp), 'short') : '' }}</td>
            <td v-text="$t('upraizApp.VoteCcy.' + accountReclaimPayout.ccy)">{{ accountReclaimPayout.ccy }}</td>
            <td>{{ accountReclaimPayout.txnRef }}</td>
            <td>
              <div v-if="accountReclaimPayout.managedAccount">
                <router-link :to="{ name: 'ManagedAccountView', params: { managedAccountId: accountReclaimPayout.managedAccount.id } }">{{
                  accountReclaimPayout.managedAccount.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="accountReclaimPayout.voteManager">
                <router-link :to="{ name: 'VoteManagerView', params: { voteManagerId: accountReclaimPayout.voteManager.id } }">{{
                  accountReclaimPayout.voteManager.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'AccountReclaimPayoutView', params: { accountReclaimPayoutId: accountReclaimPayout.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'AccountReclaimPayoutEdit', params: { accountReclaimPayoutId: accountReclaimPayout.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(accountReclaimPayout)"
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
          id="upraizApp.accountReclaimPayout.delete.question"
          data-cy="accountReclaimPayoutDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-accountReclaimPayout-heading" v-text="$t('upraizApp.accountReclaimPayout.delete.question', { id: removeId })">
          Are you sure you want to delete this Account Reclaim Payout?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-accountReclaimPayout"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAccountReclaimPayout()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./account-reclaim-payout.component.ts"></script>
