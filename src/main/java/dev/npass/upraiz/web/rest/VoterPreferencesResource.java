package dev.npass.upraiz.web.rest;

import dev.npass.upraiz.domain.VoterPreferences;
import dev.npass.upraiz.repository.VoterPreferencesRepository;
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
 * REST controller for managing {@link dev.npass.upraiz.domain.VoterPreferences}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoterPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(VoterPreferencesResource.class);

    private static final String ENTITY_NAME = "voterPreferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoterPreferencesRepository voterPreferencesRepository;

    public VoterPreferencesResource(VoterPreferencesRepository voterPreferencesRepository) {
        this.voterPreferencesRepository = voterPreferencesRepository;
    }

    /**
     * {@code POST  /voter-preferences} : Create a new voterPreferences.
     *
     * @param voterPreferences the voterPreferences to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voterPreferences, or with status {@code 400 (Bad Request)} if the voterPreferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voter-preferences")
    public ResponseEntity<VoterPreferences> createVoterPreferences(@RequestBody VoterPreferences voterPreferences)
        throws URISyntaxException {
        log.debug("REST request to save VoterPreferences : {}", voterPreferences);
        if (voterPreferences.getId() != null) {
            throw new BadRequestAlertException("A new voterPreferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoterPreferences result = voterPreferencesRepository.save(voterPreferences);
        return ResponseEntity
            .created(new URI("/api/voter-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voter-preferences/:id} : Updates an existing voterPreferences.
     *
     * @param id the id of the voterPreferences to save.
     * @param voterPreferences the voterPreferences to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voterPreferences,
     * or with status {@code 400 (Bad Request)} if the voterPreferences is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voterPreferences couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voter-preferences/{id}")
    public ResponseEntity<VoterPreferences> updateVoterPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoterPreferences voterPreferences
    ) throws URISyntaxException {
        log.debug("REST request to update VoterPreferences : {}, {}", id, voterPreferences);
        if (voterPreferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voterPreferences.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterPreferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoterPreferences result = voterPreferencesRepository.save(voterPreferences);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voterPreferences.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voter-preferences/:id} : Partial updates given fields of an existing voterPreferences, field will ignore if it is null
     *
     * @param id the id of the voterPreferences to save.
     * @param voterPreferences the voterPreferences to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voterPreferences,
     * or with status {@code 400 (Bad Request)} if the voterPreferences is not valid,
     * or with status {@code 404 (Not Found)} if the voterPreferences is not found,
     * or with status {@code 500 (Internal Server Error)} if the voterPreferences couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voter-preferences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoterPreferences> partialUpdateVoterPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoterPreferences voterPreferences
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoterPreferences partially : {}, {}", id, voterPreferences);
        if (voterPreferences.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voterPreferences.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voterPreferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoterPreferences> result = voterPreferencesRepository
            .findById(voterPreferences.getId())
            .map(existingVoterPreferences -> {
                if (voterPreferences.getReceiveCcy() != null) {
                    existingVoterPreferences.setReceiveCcy(voterPreferences.getReceiveCcy());
                }

                return existingVoterPreferences;
            })
            .map(voterPreferencesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voterPreferences.getId().toString())
        );
    }

    /**
     * {@code GET  /voter-preferences} : get all the voterPreferences.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voterPreferences in body.
     */
    @GetMapping("/voter-preferences")
    public List<VoterPreferences> getAllVoterPreferences(@RequestParam(required = false) String filter) {
        if ("voter-is-null".equals(filter)) {
            log.debug("REST request to get all VoterPreferencess where voter is null");
            return StreamSupport
                .stream(voterPreferencesRepository.findAll().spliterator(), false)
                .filter(voterPreferences -> voterPreferences.getVoter() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all VoterPreferences");
        return voterPreferencesRepository.findAll();
    }

    /**
     * {@code GET  /voter-preferences/:id} : get the "id" voterPreferences.
     *
     * @param id the id of the voterPreferences to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voterPreferences, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voter-preferences/{id}")
    public ResponseEntity<VoterPreferences> getVoterPreferences(@PathVariable Long id) {
        log.debug("REST request to get VoterPreferences : {}", id);
        Optional<VoterPreferences> voterPreferences = voterPreferencesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(voterPreferences);
    }

    /**
     * {@code DELETE  /voter-preferences/:id} : delete the "id" voterPreferences.
     *
     * @param id the id of the voterPreferences to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voter-preferences/{id}")
    public ResponseEntity<Void> deleteVoterPreferences(@PathVariable Long id) {
        log.debug("REST request to delete VoterPreferences : {}", id);
        voterPreferencesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
