package dev.npass.repository;

import dev.npass.domain.Voter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Voter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {}
