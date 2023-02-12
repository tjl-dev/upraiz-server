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
 * A VotePayout.
 */
@Entity
@Table(name = "vote_payout")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VotePayout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "paid_time", nullable = false)
    private ZonedDateTime paidTime;

    @NotNull
    @Column(name = "paid_amount", nullable = false)
    private Double paidAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "paid_ccy", nullable = false)
    private VoteCcy paidCcy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voter", "votePayouts" }, allowSetters = true)
    private VoterAccount voterAccount;

    @JsonIgnoreProperties(value = { "voteTarget", "votePayout", "voter" }, allowSetters = true)
    @OneToOne(mappedBy = "votePayout")
    private Vote vote;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VotePayout id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPaidTime() {
        return this.paidTime;
    }

    public VotePayout paidTime(ZonedDateTime paidTime) {
        this.setPaidTime(paidTime);
        return this;
    }

    public void setPaidTime(ZonedDateTime paidTime) {
        this.paidTime = paidTime;
    }

    public Double getPaidAmount() {
        return this.paidAmount;
    }

    public VotePayout paidAmount(Double paidAmount) {
        this.setPaidAmount(paidAmount);
        return this;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public VoteCcy getPaidCcy() {
        return this.paidCcy;
    }

    public VotePayout paidCcy(VoteCcy paidCcy) {
        this.setPaidCcy(paidCcy);
        return this;
    }

    public void setPaidCcy(VoteCcy paidCcy) {
        this.paidCcy = paidCcy;
    }

    public VoterAccount getVoterAccount() {
        return this.voterAccount;
    }

    public void setVoterAccount(VoterAccount voterAccount) {
        this.voterAccount = voterAccount;
    }

    public VotePayout voterAccount(VoterAccount voterAccount) {
        this.setVoterAccount(voterAccount);
        return this;
    }

    public Vote getVote() {
        return this.vote;
    }

    public void setVote(Vote vote) {
        if (this.vote != null) {
            this.vote.setVotePayout(null);
        }
        if (vote != null) {
            vote.setVotePayout(this);
        }
        this.vote = vote;
    }

    public VotePayout vote(Vote vote) {
        this.setVote(vote);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VotePayout)) {
            return false;
        }
        return id != null && id.equals(((VotePayout) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VotePayout{" +
            "id=" + getId() +
            ", paidTime='" + getPaidTime() + "'" +
            ", paidAmount=" + getPaidAmount() +
            ", paidCcy='" + getPaidCcy() + "'" +
            "}";
    }
}
