package com.hakkinenT.selfreference.services;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.repositories.PersonRepository;
import com.hakkinenT.selfreference.tests.PersonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PersonServiceTests {
    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    private PersonDTO personDTO;

    private Person person;


    @BeforeEach
    void setUp() throws Exception{
        person = PersonFactory.createPerson("João", "Maria");

        personDTO = new PersonDTO(person);

        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);
    }

    @Test
    public void insertShouldReturnPersonDTO(){
        PersonDTO result = personService.insert(personDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), personDTO.getName());
        Assertions.assertEquals(result.getPartner().getName(), personDTO.getPartner().getName());
    }
}
