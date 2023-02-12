package dev.npass.web.rest;

import dev.npass.domain.AccountReclaimRequest;
import dev.npass.repository.AccountReclaimRequestRepository;
import dev.npass.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link dev.npass.domain.AccountReclaimRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountReclaimRequestResource {

    private final Logger log = LoggerFactory.getLogger(AccountReclaimRequestResource.class);

    private static final String ENTITY_NAME = "accountReclaimRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountReclaimRequestRepository accountReclaimRequestRepository;

    public AccountReclaimRequestResource(AccountReclaimRequestRepository accountReclaimRequestRepository) {
        this.accountReclaimRequestRepository = accountReclaimRequestRepository;
    }

    /**
     * {@code POST  /account-reclaim-requests} : Create a new accountReclaimRequest.
     *
     * @param accountReclaimRequest the accountReclaimRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountReclaimRequest, or with status {@code 400 (Bad Request)} if the accountReclaimRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-reclaim-requests")
    public ResponseEntity<AccountReclaimRequest> createAccountReclaimRequest(
        @Valid @RequestBody AccountReclaimRequest accountReclaimRequest
    ) throws URISyntaxException {
        log.debug("REST request to save AccountReclaimRequest : {}", accountReclaimRequest);
        if (accountReclaimRequest.getId() != null) {
            throw new BadRequestAlertException("A new accountReclaimRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountReclaimRequest result = accountReclaimRequestRepository.save(accountReclaimRequest);
        return ResponseEntity
            .created(new URI("/api/account-reclaim-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-reclaim-requests/:id} : Updates an existing accountReclaimRequest.
     *
     * @param id the id of the accountReclaimRequest to save.
     * @param accountReclaimRequest the accountReclaimRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountReclaimRequest,
     * or with status {@code 400 (Bad Request)} if the accountReclaimRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountReclaimRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-reclaim-requests/{id}")
    public ResponseEntity<AccountReclaimRequest> updateAccountReclaimRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountReclaimRequest accountReclaimRequest
    ) throws URISyntaxException {
        log.debug("REST request to update AccountReclaimRequest : {}, {}", id, accountReclaimRequest);
        if (accountReclaimRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountReclaimRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountReclaimRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountReclaimRequest result = accountReclaimRequestRepository.save(accountReclaimRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountReclaimRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-reclaim-requests/:id} : Partial updates given fields of an existing accountReclaimRequest, field will ignore if it is null
     *
     * @param id the id of the accountReclaimRequest to save.
     * @param accountReclaimRequest the accountReclaimRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountReclaimRequest,
     * or with status {@code 400 (Bad Request)} if the accountReclaimRequest is not valid,
     * or with status {@code 404 (Not Found)} if the accountReclaimRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountReclaimRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-reclaim-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountReclaimRequest> partialUpdateAccountReclaimRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountReclaimRequest accountReclaimRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountReclaimRequest partially : {}, {}", id, accountReclaimRequest);
        if (accountReclaimRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountReclaimRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountReclaimRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountReclaimRequest> result = accountReclaimRequestRepository
            .findById(accountReclaimRequest.getId())
            .map(existingAccountReclaimRequest -> {
                if (accountReclaimRequest.getAmount() != null) {
                    existingAccountReclaimRequest.setAmount(accountReclaimRequest.getAmount());
                }
                if (accountReclaimRequest.getTimestamp() != null) {
                    existingAccountReclaimRequest.setTimestamp(accountReclaimRequest.getTimestamp());
                }
                if (accountReclaimRequest.getCcy() != null) {
                    existingAccountReclaimRequest.setCcy(accountReclaimRequest.getCcy());
                }
                if (accountReclaimRequest.getActive() != null) {
                    existingAccountReclaimRequest.setActive(accountReclaimRequest.getActive());
                }

                return existingAccountReclaimRequest;
            })
            .map(accountReclaimRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountReclaimRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /account-reclaim-requests} : get all the accountReclaimRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountReclaimRequests in body.
     */
    @GetMapping("/account-reclaim-requests")
    public List<AccountReclaimRequest> getAllAccountReclaimRequests() {
        log.debug("REST request to get all AccountReclaimRequests");
        return accountReclaimRequestRepository.findAll();
    }

    /**
     * {@code GET  /account-reclaim-requests/:id} : get the "id" accountReclaimRequest.
     *
     * @param id the id of the accountReclaimRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountReclaimRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-reclaim-requests/{id}")
    public ResponseEntity<AccountReclaimRequest> getAccountReclaimRequest(@PathVariable Long id) {
        log.debug("REST request to get AccountReclaimRequest : {}", id);
        Optional<AccountReclaimRequest> accountReclaimRequest = accountReclaimRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountReclaimRequest);
    }

    /**
     * {@code DELETE  /account-reclaim-requests/:id} : delete the "id" accountReclaimRequest.
     *
     * @param id the id of the accountReclaimRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-reclaim-requests/{id}")
    public ResponseEntity<Void> deleteAccountReclaimRequest(@PathVariable Long id) {
        log.debug("REST request to delete AccountReclaimRequest : {}", id);
        accountReclaimRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
