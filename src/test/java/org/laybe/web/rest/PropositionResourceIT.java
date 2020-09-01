package org.laybe.web.rest;

import org.laybe.FaxiomApp;
import org.laybe.domain.Proposition;
import org.laybe.repository.PropositionRepository;
import org.laybe.service.PropositionService;

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

import org.laybe.domain.enumeration.PropositionType;
import org.laybe.domain.enumeration.ConnectionType;
/**
 * Integration tests for the {@link PropositionResource} REST controller.
 */
@SpringBootTest(classes = FaxiomApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PropositionResourceIT {

    private static final PropositionType DEFAULT_TYPE = PropositionType.CONNECTION;
    private static final PropositionType UPDATED_TYPE = PropositionType.SINGLE;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ConnectionType DEFAULT_CONNECTION_TYPE = ConnectionType.CONJUNCTION;
    private static final ConnectionType UPDATED_CONNECTION_TYPE = ConnectionType.DISJUNCTION;

    @Autowired
    private PropositionRepository propositionRepository;

    @Autowired
    private PropositionService propositionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropositionMockMvc;

    private Proposition proposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposition createEntity(EntityManager em) {
        Proposition proposition = new Proposition()
            .type(DEFAULT_TYPE)
            .text(DEFAULT_TEXT)
            .connectionType(DEFAULT_CONNECTION_TYPE);
        return proposition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposition createUpdatedEntity(EntityManager em) {
        Proposition proposition = new Proposition()
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .connectionType(UPDATED_CONNECTION_TYPE);
        return proposition;
    }

    @BeforeEach
    public void initTest() {
        proposition = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposition() throws Exception {
        int databaseSizeBeforeCreate = propositionRepository.findAll().size();
        // Create the Proposition
        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isCreated());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate + 1);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProposition.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testProposition.getConnectionType()).isEqualTo(DEFAULT_CONNECTION_TYPE);
    }

    @Test
    @Transactional
    public void createPropositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propositionRepository.findAll().size();

        // Create the Proposition with an existing ID
        proposition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPropositions() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        // Get all the propositionList
        restPropositionMockMvc.perform(get("/api/propositions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].connectionType").value(hasItem(DEFAULT_CONNECTION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getProposition() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", proposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposition.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.connectionType").value(DEFAULT_CONNECTION_TYPE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProposition() throws Exception {
        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposition() throws Exception {
        // Initialize the database
        propositionService.save(proposition);

        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Update the proposition
        Proposition updatedProposition = propositionRepository.findById(proposition.getId()).get();
        // Disconnect from session so that the updates on updatedProposition are not directly saved in db
        em.detach(updatedProposition);
        updatedProposition
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .connectionType(UPDATED_CONNECTION_TYPE);

        restPropositionMockMvc.perform(put("/api/propositions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposition)))
            .andExpect(status().isOk());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProposition.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testProposition.getConnectionType()).isEqualTo(UPDATED_CONNECTION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropositionMockMvc.perform(put("/api/propositions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProposition() throws Exception {
        // Initialize the database
        propositionService.save(proposition);

        int databaseSizeBeforeDelete = propositionRepository.findAll().size();

        // Delete the proposition
        restPropositionMockMvc.perform(delete("/api/propositions/{id}", proposition.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
