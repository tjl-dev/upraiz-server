package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManagedAccount.
 */
@Entity
@Table(name = "managed_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManagedAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ccy", nullable = false)
    private VoteCcy ccy;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "seed", nullable = false)
    private String seed;

    @JsonIgnoreProperties(value = { "managedAccount", "accountReclaimRequest", "voteManager" }, allowSetters = true)
    @OneToOne(mappedBy = "managedAccount")
    private AccountReclaimPayout accountReclaimPayout;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "voteManagerPreferences", "managedAccounts", "voteTargets", "accountReclaimRequests", "accountReclaimPayouts" },
        allowSetters = true
    )
    private VoteManager voteManager;

    @OneToMany(mappedBy = "managedAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "accountReclaimPayout", "managedAccount", "voteManager" }, allowSetters = true)
    private Set<AccountReclaimRequest> accountReclaimRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ManagedAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public ManagedAccount accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public VoteCcy getCcy() {
        return this.ccy;
    }

    public ManagedAccount ccy(VoteCcy ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(VoteCcy ccy) {
        this.ccy = ccy;
    }

    public String getAddress() {
        return this.address;
    }

    public ManagedAccount address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeed() {
        return this.seed;
    }

    public ManagedAccount seed(String seed) {
        this.setSeed(seed);
        return this;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public AccountReclaimPayout getAccountReclaimPayout() {
        return this.accountReclaimPayout;
    }

    public void setAccountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        if (this.accountReclaimPayout != null) {
            this.accountReclaimPayout.setManagedAccount(null);
        }
        if (accountReclaimPayout != null) {
            accountReclaimPayout.setManagedAccount(this);
        }
        this.accountReclaimPayout = accountReclaimPayout;
    }

    public ManagedAccount accountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        this.setAccountReclaimPayout(accountReclaimPayout);
        return this;
    }

    public VoteManager getVoteManager() {
        return this.voteManager;
    }

    public void setVoteManager(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public ManagedAccount voteManager(VoteManager voteManager) {
        this.setVoteManager(voteManager);
        return this;
    }

    public Set<AccountReclaimRequest> getAccountReclaimRequests() {
        return this.accountReclaimRequests;
    }

    public void setAccountReclaimRequests(Set<AccountReclaimRequest> accountReclaimRequests) {
        if (this.accountReclaimRequests != null) {
            this.accountReclaimRequests.forEach(i -> i.setManagedAccount(null));
        }
        if (accountReclaimRequests != null) {
            accountReclaimRequests.forEach(i -> i.setManagedAccount(this));
        }
        this.accountReclaimRequests = accountReclaimRequests;
    }

    public ManagedAccount accountReclaimRequests(Set<AccountReclaimRequest> accountReclaimRequests) {
        this.setAccountReclaimRequests(accountReclaimRequests);
        return this;
    }

    public ManagedAccount addAccountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        this.accountReclaimRequests.add(accountReclaimRequest);
        accountReclaimRequest.setManagedAccount(this);
        return this;
    }

    public ManagedAccount removeAccountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        this.accountReclaimRequests.remove(accountReclaimRequest);
        accountReclaimRequest.setManagedAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManagedAccount)) {
            return false;
        }
        return id != null && id.equals(((ManagedAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedAccount{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", ccy='" + getCcy() + "'" +
            ", address='" + getAddress() + "'" +
            ", seed='" + getSeed() + "'" +
            "}";
    }
}
