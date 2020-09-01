package org.laybe.web.rest;

import org.laybe.domain.Argument;
import org.laybe.service.ArgumentService;
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
 * REST controller for managing {@link org.laybe.domain.Argument}.
 */
@RestController
@RequestMapping("/api")
public class ArgumentResource {

    private final Logger log = LoggerFactory.getLogger(ArgumentResource.class);

    private static final String ENTITY_NAME = "argument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArgumentService argumentService;

    public ArgumentResource(ArgumentService argumentService) {
        this.argumentService = argumentService;
    }

    /**
     * {@code POST  /arguments} : Create a new argument.
     *
     * @param argument the argument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new argument, or with status {@code 400 (Bad Request)} if the argument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arguments")
    public ResponseEntity<Argument> createArgument(@RequestBody Argument argument) throws URISyntaxException {
        log.debug("REST request to save Argument : {}", argument);
        if (argument.getId() != null) {
            throw new BadRequestAlertException("A new argument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Argument result = argumentService.save(argument);
        return ResponseEntity.created(new URI("/api/arguments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arguments} : Updates an existing argument.
     *
     * @param argument the argument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated argument,
     * or with status {@code 400 (Bad Request)} if the argument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the argument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arguments")
    public ResponseEntity<Argument> updateArgument(@RequestBody Argument argument) throws URISyntaxException {
        log.debug("REST request to update Argument : {}", argument);
        if (argument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Argument result = argumentService.save(argument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, argument.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arguments} : get all the arguments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arguments in body.
     */
    @GetMapping("/arguments")
    public ResponseEntity<List<Argument>> getAllArguments(Pageable pageable) {
        log.debug("REST request to get a page of Arguments");
        Page<Argument> page = argumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arguments/:id} : get the "id" argument.
     *
     * @param id the id of the argument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the argument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arguments/{id}")
    public ResponseEntity<Argument> getArgument(@PathVariable Long id) {
        log.debug("REST request to get Argument : {}", id);
        Optional<Argument> argument = argumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(argument);
    }

    /**
     * {@code DELETE  /arguments/:id} : delete the "id" argument.
     *
     * @param id the id of the argument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arguments/{id}")
    public ResponseEntity<Void> deleteArgument(@PathVariable Long id) {
        log.debug("REST request to delete Argument : {}", id);
        argumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
