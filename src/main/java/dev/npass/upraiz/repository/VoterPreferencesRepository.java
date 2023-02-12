package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.VoterPreferences;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoterPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoterPreferencesRepository extends JpaRepository<VoterPreferences, Long> {}
