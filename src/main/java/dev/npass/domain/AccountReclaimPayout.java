package dev.npass.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.domain.enumeration.VoteCcy;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountReclaimPayout.
 */
@Entity
@Table(name = "account_reclaim_payout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountReclaimPayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ccy", nullable = false)
    private VoteCcy ccy;

    @NotNull
    @Column(name = "txn_ref", nullable = false)
    private String txnRef;

    @JsonIgnoreProperties(value = { "accountReclaimPayout", "voteManager", "accountReclaimRequests" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ManagedAccount managedAccount;

    @JsonIgnoreProperties(value = { "accountReclaimPayout", "managedAccount", "voteManager" }, allowSetters = true)
    @OneToOne(mappedBy = "accountReclaimPayout")
    private AccountReclaimRequest accountReclaimRequest;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "voteManagerPreferences", "managedAccounts", "voteTargets", "accountReclaimRequests", "accountReclaimPayouts" },
        allowSetters = true
    )
    private VoteManager voteManager;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountReclaimPayout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public AccountReclaimPayout amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public AccountReclaimPayout timestamp(ZonedDateTime timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public VoteCcy getCcy() {
        return this.ccy;
    }

    public AccountReclaimPayout ccy(VoteCcy ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(VoteCcy ccy) {
        this.ccy = ccy;
    }

    public String getTxnRef() {
        return this.txnRef;
    }

    public AccountReclaimPayout txnRef(String txnRef) {
        this.setTxnRef(txnRef);
        return this;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public ManagedAccount getManagedAccount() {
        return this.managedAccount;
    }

    public void setManagedAccount(ManagedAccount managedAccount) {
        this.managedAccount = managedAccount;
    }

    public AccountReclaimPayout managedAccount(ManagedAccount managedAccount) {
        this.setManagedAccount(managedAccount);
        return this;
    }

    public AccountReclaimRequest getAccountReclaimRequest() {
        return this.accountReclaimRequest;
    }

    public void setAccountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        if (this.accountReclaimRequest != null) {
            this.accountReclaimRequest.setAccountReclaimPayout(null);
        }
        if (accountReclaimRequest != null) {
            accountReclaimRequest.setAccountReclaimPayout(this);
        }
        this.accountReclaimRequest = accountReclaimRequest;
    }

    public AccountReclaimPayout accountReclaimRequest(AccountReclaimRequest accountReclaimRequest) {
        this.setAccountReclaimRequest(accountReclaimRequest);
        return this;
    }

    public VoteManager getVoteManager() {
        return this.voteManager;
    }

    public void setVoteManager(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public AccountReclaimPayout voteManager(VoteManager voteManager) {
        this.setVoteManager(voteManager);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountReclaimPayout)) {
            return false;
        }
        return id != null && id.equals(((AccountReclaimPayout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountReclaimPayout{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", timestamp='" + getTimestamp() + "'" +
            ", ccy='" + getCcy() + "'" +
            ", txnRef='" + getTxnRef() + "'" +
            "}";
    }
}
