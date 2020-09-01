package org.laybe.web.rest;

import org.laybe.FaxiomApp;
import org.laybe.domain.Argument;
import org.laybe.repository.ArgumentRepository;
import org.laybe.service.ArgumentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.laybe.domain.enumeration.ArgumentType;
/**
 * Integration tests for the {@link ArgumentResource} REST controller.
 */
@SpringBootTest(classes = FaxiomApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArgumentResourceIT {

    private static final ArgumentType DEFAULT_TYPE = ArgumentType.IMPLICATION;
    private static final ArgumentType UPDATED_TYPE = ArgumentType.ABJUNCTION;

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private ArgumentService argumentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArgumentMockMvc;

    private Argument argument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Argument createEntity(EntityManager em) {
        Argument argument = new Argument()
            .type(DEFAULT_TYPE);
        return argument;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Argument createUpdatedEntity(EntityManager em) {
        Argument argument = new Argument()
            .type(UPDATED_TYPE);
        return argument;
    }

    @BeforeEach
    public void initTest() {
        argument = createEntity(em);
    }

    @Test
    @Transactional
    public void createArgument() throws Exception {
        int databaseSizeBeforeCreate = argumentRepository.findAll().size();
        // Create the Argument
        restArgumentMockMvc.perform(post("/api/arguments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(argument)))
            .andExpect(status().isCreated());

        // Validate the Argument in the database
        List<Argument> argumentList = argumentRepository.findAll();
        assertThat(argumentList).hasSize(databaseSizeBeforeCreate + 1);
        Argument testArgument = argumentList.get(argumentList.size() - 1);
        assertThat(testArgument.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createArgumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = argumentRepository.findAll().size();

        // Create the Argument with an existing ID
        argument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArgumentMockMvc.perform(post("/api/arguments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(argument)))
            .andExpect(status().isBadRequest());

        // Validate the Argument in the database
        List<Argument> argumentList = argumentRepository.findAll();
        assertThat(argumentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArguments() throws Exception {
        // Initialize the database
        argumentRepository.saveAndFlush(argument);

        // Get all the argumentList
        restArgumentMockMvc.perform(get("/api/arguments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(argument.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getArgument() throws Exception {
        // Initialize the database
        argumentRepository.saveAndFlush(argument);

        // Get the argument
        restArgumentMockMvc.perform(get("/api/arguments/{id}", argument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(argument.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingArgument() throws Exception {
        // Get the argument
        restArgumentMockMvc.perform(get("/api/arguments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArgument() throws Exception {
        // Initialize the database
        argumentService.save(argument);

        int databaseSizeBeforeUpdate = argumentRepository.findAll().size();

        // Update the argument
        Argument updatedArgument = argumentRepository.findById(argument.getId()).get();
        // Disconnect from session so that the updates on updatedArgument are not directly saved in db
        em.detach(updatedArgument);
        updatedArgument
            .type(UPDATED_TYPE);

        restArgumentMockMvc.perform(put("/api/arguments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArgument)))
            .andExpect(status().isOk());

        // Validate the Argument in the database
        List<Argument> argumentList = argumentRepository.findAll();
        assertThat(argumentList).hasSize(databaseSizeBeforeUpdate);
        Argument testArgument = argumentList.get(argumentList.size() - 1);
        assertThat(testArgument.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingArgument() throws Exception {
        int databaseSizeBeforeUpdate = argumentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArgumentMockMvc.perform(put("/api/arguments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(argument)))
            .andExpect(status().isBadRequest());

        // Validate the Argument in the database
        List<Argument> argumentList = argumentRepository.findAll();
        assertThat(argumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArgument() throws Exception {
        // Initialize the database
        argumentService.save(argument);

        int databaseSizeBeforeDelete = argumentRepository.findAll().size();

        // Delete the argument
        restArgumentMockMvc.perform(delete("/api/arguments/{id}", argument.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Argument> argumentList = argumentRepository.findAll();
        assertThat(argumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
