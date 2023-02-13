package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "voted_timestamp", nullable = false)
    private ZonedDateTime votedTimestamp;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verified_time")
    private ZonedDateTime verifiedTime;

    @Column(name = "verified_by")
    private String verifiedBy;

    @NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @JsonIgnoreProperties(value = { "vote", "voteManager" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private VoteTarget voteTarget;

    @JsonIgnoreProperties(value = { "voterAccount", "vote" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private VotePayout votePayout;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voterPreferences", "voterAccounts", "votes" }, allowSetters = true)
    private Voter voter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getVotedTimestamp() {
        return this.votedTimestamp;
    }

    public Vote votedTimestamp(ZonedDateTime votedTimestamp) {
        this.setVotedTimestamp(votedTimestamp);
        return this;
    }

    public void setVotedTimestamp(ZonedDateTime votedTimestamp) {
        this.votedTimestamp = votedTimestamp;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public Vote verified(Boolean verified) {
        this.setVerified(verified);
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public ZonedDateTime getVerifiedTime() {
        return this.verifiedTime;
    }

    public Vote verifiedTime(ZonedDateTime verifiedTime) {
        this.setVerifiedTime(verifiedTime);
        return this;
    }

    public void setVerifiedTime(ZonedDateTime verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public String getVerifiedBy() {
        return this.verifiedBy;
    }

    public Vote verifiedBy(String verifiedBy) {
        this.setVerifiedBy(verifiedBy);
        return this;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Vote paid(Boolean paid) {
        this.setPaid(paid);
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public VoteTarget getVoteTarget() {
        return this.voteTarget;
    }

    public void setVoteTarget(VoteTarget voteTarget) {
        this.voteTarget = voteTarget;
    }

    public Vote voteTarget(VoteTarget voteTarget) {
        this.setVoteTarget(voteTarget);
        return this;
    }

    public VotePayout getVotePayout() {
        return this.votePayout;
    }

    public void setVotePayout(VotePayout votePayout) {
        this.votePayout = votePayout;
    }

    public Vote votePayout(VotePayout votePayout) {
        this.setVotePayout(votePayout);
        return this;
    }

    public Voter getVoter() {
        return this.voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public Vote voter(Voter voter) {
        this.setVoter(voter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vote)) {
            return false;
        }
        return id != null && id.equals(((Vote) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", votedTimestamp='" + getVotedTimestamp() + "'" +
            ", verified='" + getVerified() + "'" +
            ", verifiedTime='" + getVerifiedTime() + "'" +
            ", verifiedBy='" + getVerifiedBy() + "'" +
            ", paid='" + getPaid() + "'" +
            "}";
    }
}
