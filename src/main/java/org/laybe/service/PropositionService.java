package org.laybe.service;

import org.laybe.domain.Proposition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Proposition}.
 */
public interface PropositionService {

    /**
     * Save a proposition.
     *
     * @param proposition the entity to save.
     * @return the persisted entity.
     */
    Proposition save(Proposition proposition);

    /**
     * Get all the propositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Proposition> findAll(Pageable pageable);


    /**
     * Get the "id" proposition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Proposition> findOne(Long id);

    /**
     * Delete the "id" proposition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
