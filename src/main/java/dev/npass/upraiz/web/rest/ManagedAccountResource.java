package dev.npass.upraiz.web.rest;

import dev.npass.upraiz.domain.ManagedAccount;
import dev.npass.upraiz.repository.ManagedAccountRepository;
import dev.npass.upraiz.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.npass.upraiz.domain.ManagedAccount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ManagedAccountResource {

    private final Logger log = LoggerFactory.getLogger(ManagedAccountResource.class);

    private static final String ENTITY_NAME = "managedAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManagedAccountRepository managedAccountRepository;

    public ManagedAccountResource(ManagedAccountRepository managedAccountRepository) {
        this.managedAccountRepository = managedAccountRepository;
    }

    /**
     * {@code POST  /managed-accounts} : Create a new managedAccount.
     *
     * @param managedAccount the managedAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new managedAccount, or with status {@code 400 (Bad Request)} if the managedAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/managed-accounts")
    public ResponseEntity<ManagedAccount> createManagedAccount(@Valid @RequestBody ManagedAccount managedAccount)
        throws URISyntaxException {
        log.debug("REST request to save ManagedAccount : {}", managedAccount);
        if (managedAccount.getId() != null) {
            throw new BadRequestAlertException("A new managedAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManagedAccount result = managedAccountRepository.save(managedAccount);
        return ResponseEntity
            .created(new URI("/api/managed-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /managed-accounts/:id} : Updates an existing managedAccount.
     *
     * @param id the id of the managedAccount to save.
     * @param managedAccount the managedAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated managedAccount,
     * or with status {@code 400 (Bad Request)} if the managedAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the managedAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/managed-accounts/{id}")
    public ResponseEntity<ManagedAccount> updateManagedAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManagedAccount managedAccount
    ) throws URISyntaxException {
        log.debug("REST request to update ManagedAccount : {}, {}", id, managedAccount);
        if (managedAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, managedAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!managedAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManagedAccount result = managedAccountRepository.save(managedAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, managedAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /managed-accounts/:id} : Partial updates given fields of an existing managedAccount, field will ignore if it is null
     *
     * @param id the id of the managedAccount to save.
     * @param managedAccount the managedAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated managedAccount,
     * or with status {@code 400 (Bad Request)} if the managedAccount is not valid,
     * or with status {@code 404 (Not Found)} if the managedAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the managedAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/managed-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManagedAccount> partialUpdateManagedAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManagedAccount managedAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManagedAccount partially : {}, {}", id, managedAccount);
        if (managedAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, managedAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!managedAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManagedAccount> result = managedAccountRepository
            .findById(managedAccount.getId())
            .map(existingManagedAccount -> {
                if (managedAccount.getAccountName() != null) {
                    existingManagedAccount.setAccountName(managedAccount.getAccountName());
                }
                if (managedAccount.getCcy() != null) {
                    existingManagedAccount.setCcy(managedAccount.getCcy());
                }
                if (managedAccount.getAddress() != null) {
                    existingManagedAccount.setAddress(managedAccount.getAddress());
                }
                if (managedAccount.getSeed() != null) {
                    existingManagedAccount.setSeed(managedAccount.getSeed());
                }

                return existingManagedAccount;
            })
            .map(managedAccountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, managedAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /managed-accounts} : get all the managedAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of managedAccounts in body.
     */
    @GetMapping("/managed-accounts")
    public List<ManagedAccount> getAllManagedAccounts() {
        log.debug("REST request to get all ManagedAccounts");
        return managedAccountRepository.findAll();
    }

    /**
     * {@code GET  /managed-accounts/:id} : get the "id" managedAccount.
     *
     * @param id the id of the managedAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the managedAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/managed-accounts/{id}")
    public ResponseEntity<ManagedAccount> getManagedAccount(@PathVariable Long id) {
        log.debug("REST request to get ManagedAccount : {}", id);
        Optional<ManagedAccount> managedAccount = managedAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(managedAccount);
    }

    /**
     * {@code DELETE  /managed-accounts/:id} : delete the "id" managedAccount.
     *
     * @param id the id of the managedAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/managed-accounts/{id}")
    public ResponseEntity<Void> deleteManagedAccount(@PathVariable Long id) {
        log.debug("REST request to delete ManagedAccount : {}", id);
        managedAccountRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
