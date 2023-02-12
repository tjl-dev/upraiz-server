package dev.npass.repository;

import dev.npass.domain.VoterAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoterAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoterAccountRepository extends JpaRepository<VoterAccount, Long> {}
