package org.laybe.repository;

import org.laybe.domain.Proposition;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Proposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropositionRepository extends JpaRepository<Proposition, Long> {
}
