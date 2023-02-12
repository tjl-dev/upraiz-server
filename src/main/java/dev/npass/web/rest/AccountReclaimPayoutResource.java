package dev.npass.web.rest;

import dev.npass.domain.AccountReclaimPayout;
import dev.npass.repository.AccountReclaimPayoutRepository;
import dev.npass.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link dev.npass.domain.AccountReclaimPayout}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountReclaimPayoutResource {

    private final Logger log = LoggerFactory.getLogger(AccountReclaimPayoutResource.class);

    private static final String ENTITY_NAME = "accountReclaimPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountReclaimPayoutRepository accountReclaimPayoutRepository;

    public AccountReclaimPayoutResource(AccountReclaimPayoutRepository accountReclaimPayoutRepository) {
        this.accountReclaimPayoutRepository = accountReclaimPayoutRepository;
    }

    /**
     * {@code POST  /account-reclaim-payouts} : Create a new accountReclaimPayout.
     *
     * @param accountReclaimPayout the accountReclaimPayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountReclaimPayout, or with status {@code 400 (Bad Request)} if the accountReclaimPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-reclaim-payouts")
    public ResponseEntity<AccountReclaimPayout> createAccountReclaimPayout(@Valid @RequestBody AccountReclaimPayout accountReclaimPayout)
        throws URISyntaxException {
        log.debug("REST request to save AccountReclaimPayout : {}", accountReclaimPayout);
        if (accountReclaimPayout.getId() != null) {
            throw new BadRequestAlertException("A new accountReclaimPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountReclaimPayout result = accountReclaimPayoutRepository.save(accountReclaimPayout);
        return ResponseEntity
            .created(new URI("/api/account-reclaim-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-reclaim-payouts/:id} : Updates an existing accountReclaimPayout.
     *
     * @param id the id of the accountReclaimPayout to save.
     * @param accountReclaimPayout the accountReclaimPayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountReclaimPayout,
     * or with status {@code 400 (Bad Request)} if the accountReclaimPayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountReclaimPayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-reclaim-payouts/{id}")
    public ResponseEntity<AccountReclaimPayout> updateAccountReclaimPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountReclaimPayout accountReclaimPayout
    ) throws URISyntaxException {
        log.debug("REST request to update AccountReclaimPayout : {}, {}", id, accountReclaimPayout);
        if (accountReclaimPayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountReclaimPayout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountReclaimPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountReclaimPayout result = accountReclaimPayoutRepository.save(accountReclaimPayout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountReclaimPayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-reclaim-payouts/:id} : Partial updates given fields of an existing accountReclaimPayout, field will ignore if it is null
     *
     * @param id the id of the accountReclaimPayout to save.
     * @param accountReclaimPayout the accountReclaimPayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountReclaimPayout,
     * or with status {@code 400 (Bad Request)} if the accountReclaimPayout is not valid,
     * or with status {@code 404 (Not Found)} if the accountReclaimPayout is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountReclaimPayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-reclaim-payouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountReclaimPayout> partialUpdateAccountReclaimPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountReclaimPayout accountReclaimPayout
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountReclaimPayout partially : {}, {}", id, accountReclaimPayout);
        if (accountReclaimPayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountReclaimPayout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountReclaimPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountReclaimPayout> result = accountReclaimPayoutRepository
            .findById(accountReclaimPayout.getId())
            .map(existingAccountReclaimPayout -> {
                if (accountReclaimPayout.getAmount() != null) {
                    existingAccountReclaimPayout.setAmount(accountReclaimPayout.getAmount());
                }
                if (accountReclaimPayout.getTimestamp() != null) {
                    existingAccountReclaimPayout.setTimestamp(accountReclaimPayout.getTimestamp());
                }
                if (accountReclaimPayout.getCcy() != null) {
                    existingAccountReclaimPayout.setCcy(accountReclaimPayout.getCcy());
                }
                if (accountReclaimPayout.getTxnRef() != null) {
                    existingAccountReclaimPayout.setTxnRef(accountReclaimPayout.getTxnRef());
                }

                return existingAccountReclaimPayout;
            })
            .map(accountReclaimPayoutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountReclaimPayout.getId().toString())
        );
    }

    /**
     * {@code GET  /account-reclaim-payouts} : get all the accountReclaimPayouts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountReclaimPayouts in body.
     */
    @GetMapping("/account-reclaim-payouts")
    public List<AccountReclaimPayout> getAllAccountReclaimPayouts(@RequestParam(required = false) String filter) {
        if ("accountreclaimrequest-is-null".equals(filter)) {
            log.debug("REST request to get all AccountReclaimPayouts where accountReclaimRequest is null");
            return StreamSupport
                .stream(accountReclaimPayoutRepository.findAll().spliterator(), false)
                .filter(accountReclaimPayout -> accountReclaimPayout.getAccountReclaimRequest() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all AccountReclaimPayouts");
        return accountReclaimPayoutRepository.findAll();
    }

    /**
     * {@code GET  /account-reclaim-payouts/:id} : get the "id" accountReclaimPayout.
     *
     * @param id the id of the accountReclaimPayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountReclaimPayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-reclaim-payouts/{id}")
    public ResponseEntity<AccountReclaimPayout> getAccountReclaimPayout(@PathVariable Long id) {
        log.debug("REST request to get AccountReclaimPayout : {}", id);
        Optional<AccountReclaimPayout> accountReclaimPayout = accountReclaimPayoutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountReclaimPayout);
    }

    /**
     * {@code DELETE  /account-reclaim-payouts/:id} : delete the "id" accountReclaimPayout.
     *
     * @param id the id of the accountReclaimPayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-reclaim-payouts/{id}")
    public ResponseEntity<Void> deleteAccountReclaimPayout(@PathVariable Long id) {
        log.debug("REST request to delete AccountReclaimPayout : {}", id);
        accountReclaimPayoutRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
