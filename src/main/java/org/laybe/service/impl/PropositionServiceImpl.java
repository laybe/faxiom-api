package org.laybe.service.impl;

import org.laybe.service.PropositionService;
import org.laybe.domain.Proposition;
import org.laybe.repository.PropositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Proposition}.
 */
@Service
@Transactional
public class PropositionServiceImpl implements PropositionService {

    private final Logger log = LoggerFactory.getLogger(PropositionServiceImpl.class);

    private final PropositionRepository propositionRepository;

    public PropositionServiceImpl(PropositionRepository propositionRepository) {
        this.propositionRepository = propositionRepository;
    }

    @Override
    public Proposition save(Proposition proposition) {
        log.debug("Request to save Proposition : {}", proposition);
        return propositionRepository.save(proposition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proposition> findAll(Pageable pageable) {
        log.debug("Request to get all Propositions");
        return propositionRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Proposition> findOne(Long id) {
        log.debug("Request to get Proposition : {}", id);
        return propositionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Proposition : {}", id);
        propositionRepository.deleteById(id);
    }
}
