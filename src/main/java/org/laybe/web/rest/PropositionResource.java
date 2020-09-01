package org.laybe.web.rest;

import org.laybe.domain.Proposition;
import org.laybe.service.PropositionService;
import org.laybe.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.laybe.domain.Proposition}.
 */
@RestController
@RequestMapping("/api")
public class PropositionResource {

    private final Logger log = LoggerFactory.getLogger(PropositionResource.class);

    private static final String ENTITY_NAME = "proposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropositionService propositionService;

    public PropositionResource(PropositionService propositionService) {
        this.propositionService = propositionService;
    }

    /**
     * {@code POST  /propositions} : Create a new proposition.
     *
     * @param proposition the proposition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposition, or with status {@code 400 (Bad Request)} if the proposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/propositions")
    public ResponseEntity<Proposition> createProposition(@RequestBody Proposition proposition) throws URISyntaxException {
        log.debug("REST request to save Proposition : {}", proposition);
        if (proposition.getId() != null) {
            throw new BadRequestAlertException("A new proposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proposition result = propositionService.save(proposition);
        return ResponseEntity.created(new URI("/api/propositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /propositions} : Updates an existing proposition.
     *
     * @param proposition the proposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposition,
     * or with status {@code 400 (Bad Request)} if the proposition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/propositions")
    public ResponseEntity<Proposition> updateProposition(@RequestBody Proposition proposition) throws URISyntaxException {
        log.debug("REST request to update Proposition : {}", proposition);
        if (proposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Proposition result = propositionService.save(proposition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proposition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /propositions} : get all the propositions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propositions in body.
     */
    @GetMapping("/propositions")
    public ResponseEntity<List<Proposition>> getAllPropositions(Pageable pageable) {
        log.debug("REST request to get a page of Propositions");
        Page<Proposition> page = propositionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /propositions/:id} : get the "id" proposition.
     *
     * @param id the id of the proposition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/propositions/{id}")
    public ResponseEntity<Proposition> getProposition(@PathVariable Long id) {
        log.debug("REST request to get Proposition : {}", id);
        Optional<Proposition> proposition = propositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proposition);
    }

    /**
     * {@code DELETE  /propositions/:id} : delete the "id" proposition.
     *
     * @param id the id of the proposition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/propositions/{id}")
    public ResponseEntity<Void> deleteProposition(@PathVariable Long id) {
        log.debug("REST request to delete Proposition : {}", id);
        propositionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
