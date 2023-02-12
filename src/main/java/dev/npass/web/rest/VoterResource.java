package dev.npass.web.rest;

import dev.npass.domain.Voter;
import dev.npass.repository.VoterRepository;
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
 * REST controller for managing {@link dev.npass.domain.Voter}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoterResource {

    private final Logger log = LoggerFactory.getLogger(VoterResource.class);

    private static final String ENTITY_NAME = "voter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoterRepository voterRepository;

    public VoterResource(VoterRepository voterRepository) {
        this.voterRepository = voterRepository;
    }

    /**
     * {@code POST  /voters} : Create a new voter.
     *
     * @param voter the voter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voter, or with status {@code 400 (Bad Request)} if the voter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voters")
    public ResponseEntity<Voter> createVoter(@Valid @RequestBody Voter voter) throws URISyntaxException {
        log.debug("REST request to save Voter : {}", voter);
        if (voter.getId() != null) {
            throw new BadRequestAlertException("A new voter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voter result = voterRepository.save(voter);
        return ResponseEntity
            .created(new URI("/api/voters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voters/:id} : Updates an existing voter.
     *
     * @param id the id of the voter to save.
     * @param voter the voter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voter,
     * or with status {@code 400 (Bad Request)} if the voter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voters/{id}")
    public ResponseEntity<Voter> updateVoter(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Voter voter)
        throws URISyntaxException {
        log.debug("REST request to update Voter : {}, {}", id, voter);
        if (voter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Voter result = voterRepository.save(voter);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voter.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voters/:id} : Partial updates given fields of an existing voter, field will ignore if it is null
     *
     * @param id the id of the voter to save.
     * @param voter the voter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voter,
     * or with status {@code 400 (Bad Request)} if the voter is not valid,
     * or with status {@code 404 (Not Found)} if the voter is not found,
     * or with status {@code 500 (Internal Server Error)} if the voter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Voter> partialUpdateVoter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Voter voter
    ) throws URISyntaxException {
        log.debug("REST request to partial update Voter partially : {}, {}", id, voter);
        if (voter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Voter> result = voterRepository
            .findById(voter.getId())
            .map(existingVoter -> {
                if (voter.getEmail() != null) {
                    existingVoter.setEmail(voter.getEmail());
                }
                if (voter.getName() != null) {
                    existingVoter.setName(voter.getName());
                }
                if (voter.getCreated() != null) {
                    existingVoter.setCreated(voter.getCreated());
                }
                if (voter.getActive() != null) {
                    existingVoter.setActive(voter.getActive());
                }
                if (voter.getComment() != null) {
                    existingVoter.setComment(voter.getComment());
                }

                return existingVoter;
            })
            .map(voterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voter.getId().toString())
        );
    }

    /**
     * {@code GET  /voters} : get all the voters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voters in body.
     */
    @GetMapping("/voters")
    public List<Voter> getAllVoters() {
        log.debug("REST request to get all Voters");
        return voterRepository.findAll();
    }

    /**
     * {@code GET  /voters/:id} : get the "id" voter.
     *
     * @param id the id of the voter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voters/{id}")
    public ResponseEntity<Voter> getVoter(@PathVariable Long id) {
        log.debug("REST request to get Voter : {}", id);
        Optional<Voter> voter = voterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voter);
    }

    /**
     * {@code DELETE  /voters/:id} : delete the "id" voter.
     *
     * @param id the id of the voter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voters/{id}")
    public ResponseEntity<Void> deleteVoter(@PathVariable Long id) {
        log.debug("REST request to delete Voter : {}", id);
        voterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
