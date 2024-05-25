package com.hakkinenT.selfreference.services;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.repositories.PersonRepository;
import com.hakkinenT.selfreference.services.exceptions.DatabaseException;
import com.hakkinenT.selfreference.services.exceptions.ResourceNotFoundException;
import com.hakkinenT.selfreference.tests.PersonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PersonServiceTests {
    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    private PersonDTO personDTO;

    private Person person;

    private Long existingPersonId;
    private Long nonExistingPersonId;

    private Long dependentId;

    private Long existingPartnerId;
    private Long nonExistingPartnerId;

    @BeforeEach
    void setUp() throws Exception{
        existingPersonId = 1L;
        existingPartnerId = 2L;

        nonExistingPersonId = 3L;
        nonExistingPartnerId = 4L;

        dependentId = 2L;

        person = PersonFactory.createPerson("João", "Maria");

        personDTO = new PersonDTO(person);

        Mockito.when(personRepository.save(Mockito.any())).thenReturn(person);

        Mockito.when(personRepository.existsById(existingPersonId)).thenReturn(true);
        Mockito.when(personRepository.existsById(dependentId)).thenReturn(true);
        Mockito.when(personRepository.existsById(nonExistingPersonId)).thenReturn(false);

        Mockito.doNothing().when(personRepository).deleteById(existingPersonId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(personRepository).deleteById(dependentId);
    }

    @Test
    public void insertShouldReturnPersonDTO(){
        PersonDTO result = personService.insert(personDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), personDTO.getName());
        Assertions.assertEquals(result.getPartner().getName(), personDTO.getPartner().getName());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            personService.delete(nonExistingPersonId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            personService.delete(existingPersonId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> {
            personService.delete(dependentId);
        });
    }
}
