package dev.npass.upraiz.web.rest;

import dev.npass.upraiz.domain.VoteManagerPreferences;
import dev.npass.upraiz.repository.VoteManagerPreferencesRepository;
import dev.npass.upraiz.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dev.npass.upraiz.domain.VoteManagerPreferences}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoteManagerPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(VoteManagerPreferencesResource.class);

    private static final String ENTITY_NAME = "voteManagerPreferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteManagerPreferencesRepository voteManagerPreferencesRepository;

    public VoteManagerPreferencesResource(VoteManagerPreferencesRepository voteManagerPreferencesRepository) {
        this.voteManagerPreferencesRepository = voteManagerPreferencesRepository;
    }

    /**
     * {@code POST  /vote-manager-preferences} : Create a new voteManagerPreferences.
     *
     * @param voteManagerPreferences the voteManagerPreferences to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voteManagerPreferences, or with status {@code 400 (Bad Request)} if the voteManagerPreferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vote-manager-preferences")
    public ResponseEntity<VoteManagerPreferences> createVoteManagerPreferences(@RequestBody VoteManagerPreferences voteManagerPreferences)
        throws URISyntaxException {
        log.debug("REST request to save VoteManagerPreferences : {}", voteManagerPreferences);
        if (voteManagerPreferences.getId() != null) {
            throw new BadRequestAlertException("A new voteManagerPreferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoteManagerPreferences result = voteManagerPreferencesRepository.save(voteManagerPreferences);
        return ResponseEntity
            .created(new URI("/api/vote-manager-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vote-manager-preferences/:id} : Updates an existing voteManagerPreferences.
     *
     * @param id the id of the voteManagerPreferences to save.
     * @param voteManagerPreferences the voteManagerPreferences to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteManagerPreferences,
     * or with status {@code 400 (Bad Request)} if the voteManagerPreferences is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voteManagerPreferences couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vote-manager-preferences/{id}")
    public ResponseEntity<VoteManagerPreferences> updateVoteManagerPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoteManagerPreferences voteManagerPreferences
    ) throws URISyntaxException {
        log.debug("REST request to update VoteManagerPreferences : {}, {}", id, voteManagerPreferences);
        if (voteManagerPreferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteManagerPreferences.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteManagerPreferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoteManagerPreferences result = voteManagerPreferencesRepository.save(voteManagerPreferences);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteManagerPreferences.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vote-manager-preferences/:id} : Partial updates given fields of an existing voteManagerPreferences, field will ignore if it is null
     *
     * @param id the id of the voteManagerPreferences to save.
     * @param voteManagerPreferences the voteManagerPreferences to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteManagerPreferences,
     * or with status {@code 400 (Bad Request)} if the voteManagerPreferences is not valid,
     * or with status {@code 404 (Not Found)} if the voteManagerPreferences is not found,
     * or with status {@code 500 (Internal Server Error)} if the voteManagerPreferences couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vote-manager-preferences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoteManagerPreferences> partialUpdateVoteManagerPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoteManagerPreferences voteManagerPreferences
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoteManagerPreferences partially : {}, {}", id, voteManagerPreferences);
        if (voteManagerPreferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteManagerPreferences.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteManagerPreferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoteManagerPreferences> result = voteManagerPreferencesRepository
            .findById(voteManagerPreferences.getId())
            .map(existingVoteManagerPreferences -> {
                if (voteManagerPreferences.getPayCcy() != null) {
                    existingVoteManagerPreferences.setPayCcy(voteManagerPreferences.getPayCcy());
                }

                return existingVoteManagerPreferences;
            })
            .map(voteManagerPreferencesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteManagerPreferences.getId().toString())
        );
    }

    /**
     * {@code GET  /vote-manager-preferences} : get all the voteManagerPreferences.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voteManagerPreferences in body.
     */
    @GetMapping("/vote-manager-preferences")
    public List<VoteManagerPreferences> getAllVoteManagerPreferences(@RequestParam(required = false) String filter) {
        if ("votemanager-is-null".equals(filter)) {
            log.debug("REST request to get all VoteManagerPreferencess where voteManager is null");
            return StreamSupport
                .stream(voteManagerPreferencesRepository.findAll().spliterator(), false)
                .filter(voteManagerPreferences -> voteManagerPreferences.getVoteManager() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all VoteManagerPreferences");
        return voteManagerPreferencesRepository.findAll();
    }

    /**
     * {@code GET  /vote-manager-preferences/:id} : get the "id" voteManagerPreferences.
     *
     * @param id the id of the voteManagerPreferences to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voteManagerPreferences, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vote-manager-preferences/{id}")
    public ResponseEntity<VoteManagerPreferences> getVoteManagerPreferences(@PathVariable Long id) {
        log.debug("REST request to get VoteManagerPreferences : {}", id);
        Optional<VoteManagerPreferences> voteManagerPreferences = voteManagerPreferencesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voteManagerPreferences);
    }

    /**
     * {@code DELETE  /vote-manager-preferences/:id} : delete the "id" voteManagerPreferences.
     *
     * @param id the id of the voteManagerPreferences to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vote-manager-preferences/{id}")
    public ResponseEntity<Void> deleteVoteManagerPreferences(@PathVariable Long id) {
        log.debug("REST request to delete VoteManagerPreferences : {}", id);
        voteManagerPreferencesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
