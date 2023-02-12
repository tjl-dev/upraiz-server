package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.VoteManager;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoteManager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteManagerRepository extends JpaRepository<VoteManager, Long> {}
