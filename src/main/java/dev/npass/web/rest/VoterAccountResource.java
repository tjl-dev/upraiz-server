package dev.npass.web.rest;

import dev.npass.domain.VoterAccount;
import dev.npass.repository.VoterAccountRepository;
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
 * REST controller for managing {@link dev.npass.domain.VoterAccount}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoterAccountResource {

    private final Logger log = LoggerFactory.getLogger(VoterAccountResource.class);

    private static final String ENTITY_NAME = "voterAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoterAccountRepository voterAccountRepository;

    public VoterAccountResource(VoterAccountRepository voterAccountRepository) {
        this.voterAccountRepository = voterAccountRepository;
    }

    /**
     * {@code POST  /voter-accounts} : Create a new voterAccount.
     *
     * @param voterAccount the voterAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voterAccount, or with status {@code 400 (Bad Request)} if the voterAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voter-accounts")
    public ResponseEntity<VoterAccount> createVoterAccount(@Valid @RequestBody VoterAccount voterAccount) throws URISyntaxException {
        log.debug("REST request to save VoterAccount : {}", voterAccount);
        if (voterAccount.getId() != null) {
            throw new BadRequestAlertException("A new voterAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoterAccount result = voterAccountRepository.save(voterAccount);
        return ResponseEntity
            .created(new URI("/api/voter-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voter-accounts/:id} : Updates an existing voterAccount.
     *
     * @param id the id of the voterAccount to save.
     * @param voterAccount the voterAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voterAccount,
     * or with status {@code 400 (Bad Request)} if the voterAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voterAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voter-accounts/{id}")
    public ResponseEntity<VoterAccount> updateVoterAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VoterAccount voterAccount
    ) throws URISyntaxException {
        log.debug("REST request to update VoterAccount : {}, {}", id, voterAccount);
        if (voterAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voterAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoterAccount result = voterAccountRepository.save(voterAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voterAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voter-accounts/:id} : Partial updates given fields of an existing voterAccount, field will ignore if it is null
     *
     * @param id the id of the voterAccount to save.
     * @param voterAccount the voterAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voterAccount,
     * or with status {@code 400 (Bad Request)} if the voterAccount is not valid,
     * or with status {@code 404 (Not Found)} if the voterAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the voterAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voter-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoterAccount> partialUpdateVoterAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VoterAccount voterAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoterAccount partially : {}, {}", id, voterAccount);
        if (voterAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voterAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoterAccount> result = voterAccountRepository
            .findById(voterAccount.getId())
            .map(existingVoterAccount -> {
                if (voterAccount.getAccountname() != null) {
                    existingVoterAccount.setAccountname(voterAccount.getAccountname());
                }
                if (voterAccount.getCcy() != null) {
                    existingVoterAccount.setCcy(voterAccount.getCcy());
                }
                if (voterAccount.getAddress() != null) {
                    existingVoterAccount.setAddress(voterAccount.getAddress());
                }

                return existingVoterAccount;
            })
            .map(voterAccountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voterAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /voter-accounts} : get all the voterAccounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voterAccounts in body.
     */
    @GetMapping("/voter-accounts")
    public List<VoterAccount> getAllVoterAccounts() {
        log.debug("REST request to get all VoterAccounts");
        return voterAccountRepository.findAll();
    }

    /**
     * {@code GET  /voter-accounts/:id} : get the "id" voterAccount.
     *
     * @param id the id of the voterAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voterAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voter-accounts/{id}")
    public ResponseEntity<VoterAccount> getVoterAccount(@PathVariable Long id) {
        log.debug("REST request to get VoterAccount : {}", id);
        Optional<VoterAccount> voterAccount = voterAccountRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voterAccount);
    }

    /**
     * {@code DELETE  /voter-accounts/:id} : delete the "id" voterAccount.
     *
     * @param id the id of the voterAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voter-accounts/{id}")
    public ResponseEntity<Void> deleteVoterAccount(@PathVariable Long id) {
        log.debug("REST request to delete VoterAccount : {}", id);
        voterAccountRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
