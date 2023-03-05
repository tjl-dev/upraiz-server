package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountReclaimRequest.
 */
@Entity
@Table(name = "account_reclaim_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountReclaimRequest implements Serializable {

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

    @Column(name = "active")
    private Boolean active;

    @JsonIgnoreProperties(value = { "managedAccount", "accountReclaimRequest", "voteManager" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AccountReclaimPayout accountReclaimPayout;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voteManager", "accountReclaimRequests" }, allowSetters = true)
    private ManagedAccount managedAccount;

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

    public AccountReclaimRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public AccountReclaimRequest amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public AccountReclaimRequest timestamp(ZonedDateTime timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public VoteCcy getCcy() {
        return this.ccy;
    }

    public AccountReclaimRequest ccy(VoteCcy ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(VoteCcy ccy) {
        this.ccy = ccy;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AccountReclaimRequest active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public AccountReclaimPayout getAccountReclaimPayout() {
        return this.accountReclaimPayout;
    }

    public void setAccountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        this.accountReclaimPayout = accountReclaimPayout;
    }

    public AccountReclaimRequest accountReclaimPayout(AccountReclaimPayout accountReclaimPayout) {
        this.setAccountReclaimPayout(accountReclaimPayout);
        return this;
    }

    public ManagedAccount getManagedAccount() {
        return this.managedAccount;
    }

    public void setManagedAccount(ManagedAccount managedAccount) {
        this.managedAccount = managedAccount;
    }

    public AccountReclaimRequest managedAccount(ManagedAccount managedAccount) {
        this.setManagedAccount(managedAccount);
        return this;
    }

    public VoteManager getVoteManager() {
        return this.voteManager;
    }

    public void setVoteManager(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public AccountReclaimRequest voteManager(VoteManager voteManager) {
        this.setVoteManager(voteManager);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountReclaimRequest)) {
            return false;
        }
        return id != null && id.equals(((AccountReclaimRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountReclaimRequest{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", timestamp='" + getTimestamp() + "'" +
            ", ccy='" + getCcy() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
