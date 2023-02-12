package dev.npass.upraiz.web.rest;

import dev.npass.upraiz.domain.VotePayout;
import dev.npass.upraiz.repository.VotePayoutRepository;
import dev.npass.upraiz.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link dev.npass.upraiz.domain.VotePayout}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VotePayoutResource {

    private final Logger log = LoggerFactory.getLogger(VotePayoutResource.class);

    private static final String ENTITY_NAME = "votePayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VotePayoutRepository votePayoutRepository;

    public VotePayoutResource(VotePayoutRepository votePayoutRepository) {
        this.votePayoutRepository = votePayoutRepository;
    }

    /**
     * {@code POST  /vote-payouts} : Create a new votePayout.
     *
     * @param votePayout the votePayout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new votePayout, or with status {@code 400 (Bad Request)} if the votePayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vote-payouts")
    public ResponseEntity<VotePayout> createVotePayout(@Valid @RequestBody VotePayout votePayout) throws URISyntaxException {
        log.debug("REST request to save VotePayout : {}", votePayout);
        if (votePayout.getId() != null) {
            throw new BadRequestAlertException("A new votePayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VotePayout result = votePayoutRepository.save(votePayout);
        return ResponseEntity
            .created(new URI("/api/vote-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vote-payouts/:id} : Updates an existing votePayout.
     *
     * @param id the id of the votePayout to save.
     * @param votePayout the votePayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated votePayout,
     * or with status {@code 400 (Bad Request)} if the votePayout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the votePayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vote-payouts/{id}")
    public ResponseEntity<VotePayout> updateVotePayout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VotePayout votePayout
    ) throws URISyntaxException {
        log.debug("REST request to update VotePayout : {}, {}", id, votePayout);
        if (votePayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, votePayout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!votePayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VotePayout result = votePayoutRepository.save(votePayout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, votePayout.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vote-payouts/:id} : Partial updates given fields of an existing votePayout, field will ignore if it is null
     *
     * @param id the id of the votePayout to save.
     * @param votePayout the votePayout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated votePayout,
     * or with status {@code 400 (Bad Request)} if the votePayout is not valid,
     * or with status {@code 404 (Not Found)} if the votePayout is not found,
     * or with status {@code 500 (Internal Server Error)} if the votePayout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vote-payouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VotePayout> partialUpdateVotePayout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VotePayout votePayout
    ) throws URISyntaxException {
        log.debug("REST request to partial update VotePayout partially : {}, {}", id, votePayout);
        if (votePayout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, votePayout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!votePayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VotePayout> result = votePayoutRepository
            .findById(votePayout.getId())
            .map(existingVotePayout -> {
                if (votePayout.getPaidTime() != null) {
                    existingVotePayout.setPaidTime(votePayout.getPaidTime());
                }
                if (votePayout.getPaidAmount() != null) {
                    existingVotePayout.setPaidAmount(votePayout.getPaidAmount());
                }
                if (votePayout.getPaidCcy() != null) {
                    existingVotePayout.setPaidCcy(votePayout.getPaidCcy());
                }

                return existingVotePayout;
            })
            .map(votePayoutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, votePayout.getId().toString())
        );
    }

    /**
     * {@code GET  /vote-payouts} : get all the votePayouts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of votePayouts in body.
     */
    @GetMapping("/vote-payouts")
    public List<VotePayout> getAllVotePayouts(@RequestParam(required = false) String filter) {
        if ("vote-is-null".equals(filter)) {
            log.debug("REST request to get all VotePayouts where vote is null");
            return StreamSupport
                .stream(votePayoutRepository.findAll().spliterator(), false)
                .filter(votePayout -> votePayout.getVote() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all VotePayouts");
        return votePayoutRepository.findAll();
    }

    /**
     * {@code GET  /vote-payouts/:id} : get the "id" votePayout.
     *
     * @param id the id of the votePayout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the votePayout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vote-payouts/{id}")
    public ResponseEntity<VotePayout> getVotePayout(@PathVariable Long id) {
        log.debug("REST request to get VotePayout : {}", id);
        Optional<VotePayout> votePayout = votePayoutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(votePayout);
    }

    /**
     * {@code DELETE  /vote-payouts/:id} : delete the "id" votePayout.
     *
     * @param id the id of the votePayout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vote-payouts/{id}")
    public ResponseEntity<Void> deleteVotePayout(@PathVariable Long id) {
        log.debug("REST request to delete VotePayout : {}", id);
        votePayoutRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
