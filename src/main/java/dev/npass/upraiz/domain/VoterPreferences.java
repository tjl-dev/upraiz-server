package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoterPreferences.
 */
@Entity
@Table(name = "voter_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoterPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "receive_ccy")
    private VoteCcy receiveCcy;

    @JsonIgnoreProperties(value = { "voterPreferences", "voterAccounts", "votes" }, allowSetters = true)
    @OneToOne(mappedBy = "voterPreferences")
    private Voter voter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoterPreferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteCcy getReceiveCcy() {
        return this.receiveCcy;
    }

    public VoterPreferences receiveCcy(VoteCcy receiveCcy) {
        this.setReceiveCcy(receiveCcy);
        return this;
    }

    public void setReceiveCcy(VoteCcy receiveCcy) {
        this.receiveCcy = receiveCcy;
    }

    public Voter getVoter() {
        return this.voter;
    }

    public void setVoter(Voter voter) {
        if (this.voter != null) {
            this.voter.setVoterPreferences(null);
        }
        if (voter != null) {
            voter.setVoterPreferences(this);
        }
        this.voter = voter;
    }

    public VoterPreferences voter(Voter voter) {
        this.setVoter(voter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoterPreferences)) {
            return false;
        }
        return id != null && id.equals(((VoterPreferences) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoterPreferences{" +
            "id=" + getId() +
            ", receiveCcy='" + getReceiveCcy() + "'" +
            "}";
    }
}
