package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoteManager.
 */
@Entity
@Table(name = "vote_manager")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoteManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "comment")
    private String comment;

    @JsonIgnoreProperties(value = { "voteManager" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private VoteManagerPreferences voteManagerPreferences;

    @OneToMany(mappedBy = "voteManager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voteManager", "accountReclaimRequests" }, allowSetters = true)
    private Set<ManagedAccount> managedAccounts = new HashSet<>();

    @OneToMany(mappedBy = "voteManager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voteManager" }, allowSetters = true)
    private Set<VoteTarget> voteTargets = new HashSet<>();

    @OneToMany(mappedBy = "voteManager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountReclaimPayout", "managedAccount", "voteManager" }, allowSetters = true)
    private Set<AccountReclaimRequest> accountReclaimRequests = new HashSet<>();

    @OneToMany(mappedBy = "voteManager")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "managedAccount", "accountReclaimRequest", "voteManager" }, allowSetters = true)
    private Set<AccountReclaimPayout> accountReclaimPayouts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoteManager id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public VoteManager email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public VoteManager name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public VoteManager created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Boolean getActive() {
        return this.active;
    }

    public VoteManager active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getComment() {
        return this.comment;
    }

    public VoteManager comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public VoteManagerPreferences getVoteManagerPreferences() {
        return this.voteManagerPreferences;
    }

    public void setVoteManagerPreferences(VoteManagerPreferences voteManagerPreferences) {
        this.voteManagerPreferences = voteManagerPreferences;
    }

    public VoteManager voteManagerPreferences(VoteManagerPreferences voteManagerPreferences) {
        this.setVoteManagerPreferences(voteManagerPreferences);
        return this;
    }

    public Set<ManagedAccount> getManagedAccounts() {
        return this.managedAccounts;
    }

    public void setManagedAccounts(Set<ManagedAccount> managedAccounts) {
        if (this.managedAccounts != null) {
            this.managedAccounts.forEach(i -> i.setVoteManager(null));
        }
        if (managedAccounts != null) {
            managedAccounts.forEach(i -> i.setVoteManager(this));
        }
        this.managedAccounts = managedAccounts;
    }

    public VoteManager managedAccounts(Set<ManagedAccount> managedAccounts) {
        this.setManagedAccounts(managedAccounts);
        return this;
    }

    public VoteManager addManagedAccount(ManagedAccount managedAccount) {
        this.managedAccounts.add(managedAccount);
        managedAccount.setVoteManager(this);
        return this;
    }

    public VoteManager removeManagedAccount(ManagedAccount managedAccount) {
        this.managedAccounts.remove(managedAccount);
        managedAccount.setVoteManager(null);
        return this;
    }

    public Set<VoteTarget> getVoteTargets() {
        return this.voteTargets;
    }

    public void setVoteTargets(Set<VoteTarget> voteTargets) {
        if (this.voteTargets != null) {
            this.voteTargets.forEach(i -> i.setVoteManager(null));
        }
        if (voteTargets != null) {
            voteTargets.forEach(i -> i.setVoteManager(this));
        }
        this.voteTargets = voteTargets;
    }

    public VoteManager voteTargets(Set<VoteTarget> voteTargets) {
        this.setVoteTargets(voteTargets);
        return this;
    }

    public VoteManager addVoteTarget(VoteTarget voteTarget) {
        this.voteTargets.add(voteTarget);
        voteTarget.setVoteManager(this);
        return this;
    }

    public VoteManager removeVoteTarget(VoteTarget voteTarget) {
        this.voteTargets.remove(voteTarget);
        voteTarget.setVoteManager(null);
        return this;
    }

    public Set<AccountReclaimRequest> getAccountReclaimRequests() {
        return this.accountReclaimRequests;
    }

    public void setAccountReclaimRequests(Set<AccountReclaimRequest> accountReclaimRequests) {
        if (this.accountReclaimRequests != null) {
            this.accountReclaimRequests.forEach(i -> i.setVoteManager(null));
        }
        if (accountReclaimRequests != null) {
            accountReclaimRequests.forEach(i -> i.setVoteManager(this));
        }
        this.accountReclaimRequests = accountReclaimRequests;
    }

    public VoteManager accountReclaimRequests(Set<AccountReclaimRequest> accountReclaimRequests) {
        this.setAccountReclaimRequests(accountReclaimRequests);
        return this;
    }

    public VoteManager addAccountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        this.accountReclaimRequests.add(accountReclaimRequest);
        accountReclaimRequest.setVoteManager(this);
        return this;
    }

    public VoteManager removeAccountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        this.accountReclaimRequests.remove(accountReclaimRequest);
        accountReclaimRequest.setVoteManager(null);
        return this;
    }

    public Set<AccountReclaimPayout> getAccountReclaimPayouts() {
        return this.accountReclaimPayouts;
    }

    public void setAccountReclaimPayouts(Set<AccountReclaimPayout> accountReclaimPayouts) {
        if (this.accountReclaimPayouts != null) {
            this.accountReclaimPayouts.forEach(i -> i.setVoteManager(null));
        }
        if (accountReclaimPayouts != null) {
            accountReclaimPayouts.forEach(i -> i.setVoteManager(this));
        }
        this.accountReclaimPayouts = accountReclaimPayouts;
    }

    public VoteManager accountReclaimPayouts(Set<AccountReclaimPayout> accountReclaimPayouts) {
        this.setAccountReclaimPayouts(accountReclaimPayouts);
        return this;
    }

    public VoteManager addAccountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        this.accountReclaimPayouts.add(accountReclaimPayout);
        accountReclaimPayout.setVoteManager(this);
        return this;
    }

    public VoteManager removeAccountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        this.accountReclaimPayouts.remove(accountReclaimPayout);
        accountReclaimPayout.setVoteManager(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoteManager)) {
            return false;
        }
        return id != null && id.equals(((VoteManager) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoteManager{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", active='" + getActive() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
