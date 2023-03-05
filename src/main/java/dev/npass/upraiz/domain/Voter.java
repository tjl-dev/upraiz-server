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
 * A Voter.
 */
@Entity
@Table(name = "voter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Voter implements Serializable {

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

    @JsonIgnoreProperties(value = { "voter" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private VoterPreferences voterPreferences;

    @OneToMany(mappedBy = "voter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voter", "votePayouts" }, allowSetters = true)
    private Set<VoterAccount> voterAccounts = new HashSet<>();

    @OneToMany(mappedBy = "voter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "votePayout", "voteTarget", "voter" }, allowSetters = true)
    private Set<Vote> votes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Voter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public Voter email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public Voter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Voter created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Voter active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getComment() {
        return this.comment;
    }

    public Voter comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public VoterPreferences getVoterPreferences() {
        return this.voterPreferences;
    }

    public void setVoterPreferences(VoterPreferences voterPreferences) {
        this.voterPreferences = voterPreferences;
    }

    public Voter voterPreferences(VoterPreferences voterPreferences) {
        this.setVoterPreferences(voterPreferences);
        return this;
    }

    public Set<VoterAccount> getVoterAccounts() {
        return this.voterAccounts;
    }

    public void setVoterAccounts(Set<VoterAccount> voterAccounts) {
        if (this.voterAccounts != null) {
            this.voterAccounts.forEach(i -> i.setVoter(null));
        }
        if (voterAccounts != null) {
            voterAccounts.forEach(i -> i.setVoter(this));
        }
        this.voterAccounts = voterAccounts;
    }

    public Voter voterAccounts(Set<VoterAccount> voterAccounts) {
        this.setVoterAccounts(voterAccounts);
        return this;
    }

    public Voter addVoterAccount(VoterAccount voterAccount) {
        this.voterAccounts.add(voterAccount);
        voterAccount.setVoter(this);
        return this;
    }

    public Voter removeVoterAccount(VoterAccount voterAccount) {
        this.voterAccounts.remove(voterAccount);
        voterAccount.setVoter(null);
        return this;
    }

    public Set<Vote> getVotes() {
        return this.votes;
    }

    public void setVotes(Set<Vote> votes) {
        if (this.votes != null) {
            this.votes.forEach(i -> i.setVoter(null));
        }
        if (votes != null) {
            votes.forEach(i -> i.setVoter(this));
        }
        this.votes = votes;
    }

    public Voter votes(Set<Vote> votes) {
        this.setVotes(votes);
        return this;
    }

    public Voter addVote(Vote vote) {
        this.votes.add(vote);
        vote.setVoter(this);
        return this;
    }

    public Voter removeVote(Vote vote) {
        this.votes.remove(vote);
        vote.setVoter(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voter)) {
            return false;
        }
        return id != null && id.equals(((Voter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voter{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", created='" + getCreated() + "'" +
            ", active='" + getActive() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
