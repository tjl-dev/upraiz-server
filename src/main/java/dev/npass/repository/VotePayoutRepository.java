package dev.npass.repository;

import dev.npass.domain.VotePayout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VotePayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VotePayoutRepository extends JpaRepository<VotePayout, Long> {}
