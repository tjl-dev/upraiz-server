package dev.npass.web.rest;

import dev.npass.domain.VoteManager;
import dev.npass.repository.VoteManagerRepository;
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
 * REST controller for managing {@link dev.npass.domain.VoteManager}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoteManagerResource {

    private final Logger log = LoggerFactory.getLogger(VoteManagerResource.class);

    private static final String ENTITY_NAME = "voteManager";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteManagerRepository voteManagerRepository;

    public VoteManagerResource(VoteManagerRepository voteManagerRepository) {
        this.voteManagerRepository = voteManagerRepository;
    }

    /**
     * {@code POST  /vote-managers} : Create a new voteManager.
     *
     * @param voteManager the voteManager to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voteManager, or with status {@code 400 (Bad Request)} if the voteManager has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vote-managers")
    public ResponseEntity<VoteManager> createVoteManager(@Valid @RequestBody VoteManager voteManager) throws URISyntaxException {
        log.debug("REST request to save VoteManager : {}", voteManager);
        if (voteManager.getId() != null) {
            throw new BadRequestAlertException("A new voteManager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoteManager result = voteManagerRepository.save(voteManager);
        return ResponseEntity
            .created(new URI("/api/vote-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vote-managers/:id} : Updates an existing voteManager.
     *
     * @param id the id of the voteManager to save.
     * @param voteManager the voteManager to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteManager,
     * or with status {@code 400 (Bad Request)} if the voteManager is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voteManager couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vote-managers/{id}")
    public ResponseEntity<VoteManager> updateVoteManager(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VoteManager voteManager
    ) throws URISyntaxException {
        log.debug("REST request to update VoteManager : {}, {}", id, voteManager);
        if (voteManager.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteManager.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteManagerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoteManager result = voteManagerRepository.save(voteManager);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteManager.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vote-managers/:id} : Partial updates given fields of an existing voteManager, field will ignore if it is null
     *
     * @param id the id of the voteManager to save.
     * @param voteManager the voteManager to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteManager,
     * or with status {@code 400 (Bad Request)} if the voteManager is not valid,
     * or with status {@code 404 (Not Found)} if the voteManager is not found,
     * or with status {@code 500 (Internal Server Error)} if the voteManager couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vote-managers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoteManager> partialUpdateVoteManager(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VoteManager voteManager
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoteManager partially : {}, {}", id, voteManager);
        if (voteManager.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteManager.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteManagerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoteManager> result = voteManagerRepository
            .findById(voteManager.getId())
            .map(existingVoteManager -> {
                if (voteManager.getEmail() != null) {
                    existingVoteManager.setEmail(voteManager.getEmail());
                }
                if (voteManager.getName() != null) {
                    existingVoteManager.setName(voteManager.getName());
                }
                if (voteManager.getCreated() != null) {
                    existingVoteManager.setCreated(voteManager.getCreated());
                }
                if (voteManager.getActive() != null) {
                    existingVoteManager.setActive(voteManager.getActive());
                }
                if (voteManager.getComment() != null) {
                    existingVoteManager.setComment(voteManager.getComment());
                }

                return existingVoteManager;
            })
            .map(voteManagerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteManager.getId().toString())
        );
    }

    /**
     * {@code GET  /vote-managers} : get all the voteManagers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voteManagers in body.
     */
    @GetMapping("/vote-managers")
    public List<VoteManager> getAllVoteManagers() {
        log.debug("REST request to get all VoteManagers");
        return voteManagerRepository.findAll();
    }

    /**
     * {@code GET  /vote-managers/:id} : get the "id" voteManager.
     *
     * @param id the id of the voteManager to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voteManager, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vote-managers/{id}")
    public ResponseEntity<VoteManager> getVoteManager(@PathVariable Long id) {
        log.debug("REST request to get VoteManager : {}", id);
        Optional<VoteManager> voteManager = voteManagerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voteManager);
    }

    /**
     * {@code DELETE  /vote-managers/:id} : delete the "id" voteManager.
     *
     * @param id the id of the voteManager to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vote-managers/{id}")
    public ResponseEntity<Void> deleteVoteManager(@PathVariable Long id) {
        log.debug("REST request to delete VoteManager : {}", id);
        voteManagerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
