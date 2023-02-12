package dev.npass.repository;

import dev.npass.domain.AccountReclaimPayout;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountReclaimPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountReclaimPayoutRepository extends JpaRepository<AccountReclaimPayout, Long> {}
