package dev.npass.repository;

import dev.npass.domain.AccountReclaimRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountReclaimRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountReclaimRequestRepository extends JpaRepository<AccountReclaimRequest, Long> {}
