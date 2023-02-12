package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.VoteTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoteTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteTargetRepository extends JpaRepository<VoteTarget, Long> {}
