package org.laybe.service;

import org.laybe.domain.Argument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Argument}.
 */
public interface ArgumentService {

    /**
     * Save a argument.
     *
     * @param argument the entity to save.
     * @return the persisted entity.
     */
    Argument save(Argument argument);

    /**
     * Get all the arguments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Argument> findAll(Pageable pageable);


    /**
     * Get the "id" argument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Argument> findOne(Long id);

    /**
     * Delete the "id" argument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
