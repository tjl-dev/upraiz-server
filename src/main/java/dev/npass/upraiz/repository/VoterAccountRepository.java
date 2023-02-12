package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.VoterAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoterAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoterAccountRepository extends JpaRepository<VoterAccount, Long> {}
