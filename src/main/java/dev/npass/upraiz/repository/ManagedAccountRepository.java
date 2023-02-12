package dev.npass.upraiz.repository;

import dev.npass.upraiz.domain.ManagedAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ManagedAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManagedAccountRepository extends JpaRepository<ManagedAccount, Long> {}
