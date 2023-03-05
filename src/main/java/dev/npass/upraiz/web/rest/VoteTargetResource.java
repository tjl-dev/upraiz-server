package dev.npass.upraiz.web.rest;

import dev.npass.upraiz.domain.VoteTarget;
import dev.npass.upraiz.repository.VoteTargetRepository;
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
 * REST controller for managing {@link dev.npass.upraiz.domain.VoteTarget}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoteTargetResource {

    private final Logger log = LoggerFactory.getLogger(VoteTargetResource.class);

    private static final String ENTITY_NAME = "voteTarget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteTargetRepository voteTargetRepository;

    public VoteTargetResource(VoteTargetRepository voteTargetRepository) {
        this.voteTargetRepository = voteTargetRepository;
    }

    /**
     * {@code POST  /vote-targets} : Create a new voteTarget.
     *
     * @param voteTarget the voteTarget to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voteTarget, or with status {@code 400 (Bad Request)} if the voteTarget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vote-targets")
    public ResponseEntity<VoteTarget> createVoteTarget(@Valid @RequestBody VoteTarget voteTarget) throws URISyntaxException {
        log.debug("REST request to save VoteTarget : {}", voteTarget);
        if (voteTarget.getId() != null) {
            throw new BadRequestAlertException("A new voteTarget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoteTarget result = voteTargetRepository.save(voteTarget);
        return ResponseEntity
            .created(new URI("/api/vote-targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vote-targets/:id} : Updates an existing voteTarget.
     *
     * @param id the id of the voteTarget to save.
     * @param voteTarget the voteTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteTarget,
     * or with status {@code 400 (Bad Request)} if the voteTarget is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voteTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vote-targets/{id}")
    public ResponseEntity<VoteTarget> updateVoteTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VoteTarget voteTarget
    ) throws URISyntaxException {
        log.debug("REST request to update VoteTarget : {}, {}", id, voteTarget);
        if (voteTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoteTarget result = voteTargetRepository.save(voteTarget);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteTarget.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vote-targets/:id} : Partial updates given fields of an existing voteTarget, field will ignore if it is null
     *
     * @param id the id of the voteTarget to save.
     * @param voteTarget the voteTarget to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voteTarget,
     * or with status {@code 400 (Bad Request)} if the voteTarget is not valid,
     * or with status {@code 404 (Not Found)} if the voteTarget is not found,
     * or with status {@code 500 (Internal Server Error)} if the voteTarget couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vote-targets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoteTarget> partialUpdateVoteTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VoteTarget voteTarget
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoteTarget partially : {}, {}", id, voteTarget);
        if (voteTarget.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voteTarget.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteTargetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoteTarget> result = voteTargetRepository
            .findById(voteTarget.getId())
            .map(existingVoteTarget -> {
                if (voteTarget.getUrl() != null) {
                    existingVoteTarget.setUrl(voteTarget.getUrl());
                }
                if (voteTarget.getVotetype() != null) {
                    existingVoteTarget.setVotetype(voteTarget.getVotetype());
                }
                if (voteTarget.getPayout() != null) {
                    existingVoteTarget.setPayout(voteTarget.getPayout());
                }
                if (voteTarget.getCcy() != null) {
                    existingVoteTarget.setCcy(voteTarget.getCcy());
                }
                if (voteTarget.getComment() != null) {
                    existingVoteTarget.setComment(voteTarget.getComment());
                }
                if (voteTarget.getActive() != null) {
                    existingVoteTarget.setActive(voteTarget.getActive());
                }
                if (voteTarget.getFunded() != null) {
                    existingVoteTarget.setFunded(voteTarget.getFunded());
                }
                if (voteTarget.getCreated() != null) {
                    existingVoteTarget.setCreated(voteTarget.getCreated());
                }
                if (voteTarget.getExpiry() != null) {
                    existingVoteTarget.setExpiry(voteTarget.getExpiry());
                }
                if (voteTarget.getBoosted() != null) {
                    existingVoteTarget.setBoosted(voteTarget.getBoosted());
                }

                return existingVoteTarget;
            })
            .map(voteTargetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voteTarget.getId().toString())
        );
    }

    /**
     * {@code GET  /vote-targets} : get all the voteTargets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voteTargets in body.
     */
    @GetMapping("/vote-targets")
    public List<VoteTarget> getAllVoteTargets() {
        log.debug("REST request to get all VoteTargets");
        return voteTargetRepository.findAll();
    }

    /**
     * {@code GET  /vote-targets/:id} : get the "id" voteTarget.
     *
     * @param id the id of the voteTarget to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voteTarget, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vote-targets/{id}")
    public ResponseEntity<VoteTarget> getVoteTarget(@PathVariable Long id) {
        log.debug("REST request to get VoteTarget : {}", id);
        Optional<VoteTarget> voteTarget = voteTargetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voteTarget);
    }

    /**
     * {@code DELETE  /vote-targets/:id} : delete the "id" voteTarget.
     *
     * @param id the id of the voteTarget to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vote-targets/{id}")
    public ResponseEntity<Void> deleteVoteTarget(@PathVariable Long id) {
        log.debug("REST request to delete VoteTarget : {}", id);
        voteTargetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
