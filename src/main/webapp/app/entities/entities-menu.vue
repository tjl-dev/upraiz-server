<template>
  <div>
    <b-dropdown-item to="/vote-manager">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voteManager')">Vote Manager</span>
    </b-dropdown-item>
    <b-dropdown-item to="/vote-manager-preferences">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voteManagerPreferences')">Vote Manager Preferences</span>
    </b-dropdown-item>
    <b-dropdown-item to="/voter">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voter')">Voter</span>
    </b-dropdown-item>
    <b-dropdown-item to="/voter-preferences">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voterPreferences')">Voter Preferences</span>
    </b-dropdown-item>
    <b-dropdown-item to="/managed-account">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.managedAccount')">Managed Account</span>
    </b-dropdown-item>
    <b-dropdown-item to="/voter-account">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voterAccount')">Voter Account</span>
    </b-dropdown-item>
    <b-dropdown-item to="/vote-target">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.voteTarget')">Vote Target</span>
    </b-dropdown-item>
    <b-dropdown-item to="/vote">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.vote')">Vote</span>
    </b-dropdown-item>
    <b-dropdown-item to="/vote-payout">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.votePayout')">Vote Payout</span>
    </b-dropdown-item>
    <b-dropdown-item to="/account-reclaim-request">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.accountReclaimRequest')">Account Reclaim Request</span>
    </b-dropdown-item>
    <b-dropdown-item to="/account-reclaim-payout">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.accountReclaimPayout')">Account Reclaim Payout</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
