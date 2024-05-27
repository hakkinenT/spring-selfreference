package com.hakkinenT.selfreference.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.dto.PersonMinDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private Long nonExistingPersonId;

    private Long dependentId;

    private Long existingPartnerId;

    private Long existingPersonId;
    @BeforeEach
    void setUp() throws Exception{
        existingPersonId = 1L;
        existingPartnerId = 2L;

        dependentId = 1L;
        nonExistingPersonId = 10L;

    }

    @Test
    public void insertShouldInsertPersonWithoutPartner() throws Exception{


        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Paulo");

        String jsonBody = mapper.writeValueAsString(personDTO);

        ResultActions result =
                mockMvc.perform(post("/person")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.name").value("Paulo"));
        result.andExpect(jsonPath("$.partner").isEmpty());

    }

    @Test
    public void insertShouldInsertPersonWithPartner() throws Exception{


        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Ana");
        PersonMinDTO partnerDTO = new PersonMinDTO();
        partnerDTO.setId(3L);
        personDTO.setPartner(partnerDTO);

        String jsonBody = mapper.writeValueAsString(personDTO);

        ResultActions result =
                mockMvc.perform(post("/person")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.name").value("Ana"));
        result.andExpect(jsonPath("$.partner").exists());
        result.andExpect(jsonPath("$.partner.name").value("Julio"));
    }

    @Test
    public void findByIdShouldReturnPersonDTOWhenIdExists() throws Exception{
        ResultActions result = mockMvc.perform(get("/person/{id}", existingPartnerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.name").value("Maria"));
        result.andExpect(jsonPath("$.partner").exists());
        result.andExpect(jsonPath("$.partner.name").value("João"));

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        ResultActions result = mockMvc.perform(get("/person/{id}", nonExistingPersonId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnAllPerson() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/person")
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(1L));
        result.andExpect(jsonPath("$[0].name").value("João"));
        result.andExpect(jsonPath("$[1].id").value(2L));
        result.andExpect(jsonPath("$[1].name").value("Maria"));
        result.andExpect(jsonPath("$[2].id").value(3L));
        result.andExpect(jsonPath("$[2].name").value("Julio"));
    }


    @Test
    public void findPersonPartnerShouldReturnPersonPartners() throws Exception {
        ResultActions result =
                mockMvc.perform(get("/person/{id}/partner", existingPersonId)
                        .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(2L));
        result.andExpect(jsonPath("$.name").value("Maria"));

    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/person/{id}", existingPartnerId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete("/person/{id}", nonExistingPersonId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        ResultActions result = mockMvc.perform(delete("/person/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }


}
