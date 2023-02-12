package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.Vote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {}
