package dev.npass.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.npass.domain.enumeration.VoteCcy;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoterAccount.
 */
@Entity
@Table(name = "voter_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VoterAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "accountname")
    private String accountname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ccy", nullable = false)
    private VoteCcy ccy;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JsonIgnoreProperties(value = { "voterPreferences", "voterAccounts", "votes" }, allowSetters = true)
    private Voter voter;

    @OneToMany(mappedBy = "voterAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "voterAccount", "vote" }, allowSetters = true)
    private Set<VotePayout> votePayouts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VoterAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountname() {
        return this.accountname;
    }

    public VoterAccount accountname(String accountname) {
        this.setAccountname(accountname);
        return this;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public VoteCcy getCcy() {
        return this.ccy;
    }

    public VoterAccount ccy(VoteCcy ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(VoteCcy ccy) {
        this.ccy = ccy;
    }

    public String getAddress() {
        return this.address;
    }

    public VoterAccount address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Voter getVoter() {
        return this.voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public VoterAccount voter(Voter voter) {
        this.setVoter(voter);
        return this;
    }

    public Set<VotePayout> getVotePayouts() {
        return this.votePayouts;
    }

    public void setVotePayouts(Set<VotePayout> votePayouts) {
        if (this.votePayouts != null) {
            this.votePayouts.forEach(i -> i.setVoterAccount(null));
        }
        if (votePayouts != null) {
            votePayouts.forEach(i -> i.setVoterAccount(this));
        }
        this.votePayouts = votePayouts;
    }

    public VoterAccount votePayouts(Set<VotePayout> votePayouts) {
        this.setVotePayouts(votePayouts);
        return this;
    }

    public VoterAccount addVotePayout(VotePayout votePayout) {
        this.votePayouts.add(votePayout);
        votePayout.setVoterAccount(this);
        return this;
    }

    public VoterAccount removeVotePayout(VotePayout votePayout) {
        this.votePayouts.remove(votePayout);
        votePayout.setVoterAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoterAccount)) {
            return false;
        }
        return id != null && id.equals(((VoterAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoterAccount{" +
            "id=" + getId() +
            ", accountname='" + getAccountname() + "'" +
            ", ccy='" + getCcy() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
