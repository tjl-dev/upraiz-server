package dev.npass.upraiz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.upraiz.domain.enumeration.VoteCcy;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoteManagerPreferences.
 */
@Entity
@Table(name = "vote_manager_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoteManagerPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_ccy")
    private VoteCcy payCcy;

    @JsonIgnoreProperties(
        value = { "voteManagerPreferences", "managedAccounts", "voteTargets", "accountReclaimRequests", "accountReclaimPayouts" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "voteManagerPreferences")
    private VoteManager voteManager;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoteManagerPreferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoteCcy getPayCcy() {
        return this.payCcy;
    }

    public VoteManagerPreferences payCcy(VoteCcy payCcy) {
        this.setPayCcy(payCcy);
        return this;
    }

    public void setPayCcy(VoteCcy payCcy) {
        this.payCcy = payCcy;
    }

    public VoteManager getVoteManager() {
        return this.voteManager;
    }

    public void setVoteManager(VoteManager voteManager) {
        if (this.voteManager != null) {
            this.voteManager.setVoteManagerPreferences(null);
        }
        if (voteManager != null) {
            voteManager.setVoteManagerPreferences(this);
        }
        this.voteManager = voteManager;
    }

    public VoteManagerPreferences voteManager(VoteManager voteManager) {
        this.setVoteManager(voteManager);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoteManagerPreferences)) {
            return false;
        }
        return id != null && id.equals(((VoteManagerPreferences) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoteManagerPreferences{" +
            "id=" + getId() +
            ", payCcy='" + getPayCcy() + "'" +
            "}";
    }
}
