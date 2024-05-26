package com.hakkinenT.selfreference.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.dto.PersonMinDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void insertShouldInsertPerson() throws Exception{


        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Paulo");
        PersonMinDTO partnerDTO = new PersonMinDTO();
        partnerDTO.setName("Ana");
        personDTO.setPartner(partnerDTO);

        String jsonBody = mapper.writeValueAsString(personDTO);

        ResultActions result =
                mockMvc.perform(post("/person")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.name").value("Paulo"));
        result.andExpect(jsonPath("$.partner").exists());
        result.andExpect(jsonPath("$.partner.name").value("Ana"));
    }
}
