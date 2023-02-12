package dev.npass.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.domain.enumeration.VoteCcy;
import dev.npass.domain.enumeration.VoteTargetType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoteTarget.
 */
@Entity
@Table(name = "vote_target")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoteTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "votetype", nullable = false)
    private VoteTargetType votetype;

    @NotNull
    @Column(name = "payout", nullable = false)
    private Double payout;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ccy", nullable = false)
    private VoteCcy ccy;

    @Column(name = "comment")
    private String comment;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "funded")
    private Boolean funded;

    @Column(name = "created")
    private ZonedDateTime created;

    @Column(name = "expiry")
    private ZonedDateTime expiry;

    @Column(name = "boosted")
    private Boolean boosted;

    @JsonIgnoreProperties(value = { "voteTarget", "votePayout", "voter" }, allowSetters = true)
    @OneToOne(mappedBy = "voteTarget")
    private Vote vote;

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

    public VoteTarget id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public VoteTarget url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VoteTargetType getVotetype() {
        return this.votetype;
    }

    public VoteTarget votetype(VoteTargetType votetype) {
        this.setVotetype(votetype);
        return this;
    }

    public void setVotetype(VoteTargetType votetype) {
        this.votetype = votetype;
    }

    public Double getPayout() {
        return this.payout;
    }

    public VoteTarget payout(Double payout) {
        this.setPayout(payout);
        return this;
    }

    public void setPayout(Double payout) {
        this.payout = payout;
    }

    public VoteCcy getCcy() {
        return this.ccy;
    }

    public VoteTarget ccy(VoteCcy ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(VoteCcy ccy) {
        this.ccy = ccy;
    }

    public String getComment() {
        return this.comment;
    }

    public VoteTarget comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getActive() {
        return this.active;
    }

    public VoteTarget active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getFunded() {
        return this.funded;
    }

    public VoteTarget funded(Boolean funded) {
        this.setFunded(funded);
        return this;
    }

    public void setFunded(Boolean funded) {
        this.funded = funded;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public VoteTarget created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getExpiry() {
        return this.expiry;
    }

    public VoteTarget expiry(ZonedDateTime expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public Boolean getBoosted() {
        return this.boosted;
    }

    public VoteTarget boosted(Boolean boosted) {
        this.setBoosted(boosted);
        return this;
    }

    public void setBoosted(Boolean boosted) {
        this.boosted = boosted;
    }

    public Vote getVote() {
        return this.vote;
    }

    public void setVote(Vote vote) {
        if (this.vote != null) {
            this.vote.setVoteTarget(null);
        }
        if (vote != null) {
            vote.setVoteTarget(this);
        }
        this.vote = vote;
    }

    public VoteTarget vote(Vote vote) {
        this.setVote(vote);
        return this;
    }

    public VoteManager getVoteManager() {
        return this.voteManager;
    }

    public void setVoteManager(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public VoteTarget voteManager(VoteManager voteManager) {
        this.setVoteManager(voteManager);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoteTarget)) {
            return false;
        }
        return id != null && id.equals(((VoteTarget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoteTarget{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", votetype='" + getVotetype() + "'" +
            ", payout=" + getPayout() +
            ", ccy='" + getCcy() + "'" +
            ", comment='" + getComment() + "'" +
            ", active='" + getActive() + "'" +
            ", funded='" + getFunded() + "'" +
            ", created='" + getCreated() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", boosted='" + getBoosted() + "'" +
            "}";
    }
}
