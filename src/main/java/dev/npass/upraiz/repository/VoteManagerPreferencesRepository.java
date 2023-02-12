package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.VoteManagerPreferences;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoteManagerPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteManagerPreferencesRepository extends JpaRepository<VoteManagerPreferences, Long> {}
