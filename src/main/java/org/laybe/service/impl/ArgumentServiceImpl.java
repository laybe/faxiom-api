package org.laybe.service.impl;

import org.laybe.service.ArgumentService;
import org.laybe.domain.Argument;
import org.laybe.repository.ArgumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Argument}.
 */
@Service
@Transactional
public class ArgumentServiceImpl implements ArgumentService {

    private final Logger log = LoggerFactory.getLogger(ArgumentServiceImpl.class);

    private final ArgumentRepository argumentRepository;

    public ArgumentServiceImpl(ArgumentRepository argumentRepository) {
        this.argumentRepository = argumentRepository;
    }

    @Override
    public Argument save(Argument argument) {
        log.debug("Request to save Argument : {}", argument);
        return argumentRepository.save(argument);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Argument> findAll(Pageable pageable) {
        log.debug("Request to get all Arguments");
        return argumentRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Argument> findOne(Long id) {
        log.debug("Request to get Argument : {}", id);
        return argumentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Argument : {}", id);
        argumentRepository.deleteById(id);
    }
}
