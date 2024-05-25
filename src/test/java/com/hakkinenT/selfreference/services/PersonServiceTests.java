package com.hakkinenT.selfreference.services;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.repositories.PersonRepository;
import com.hakkinenT.selfreference.services.exceptions.DatabaseException;
import com.hakkinenT.selfreference.services.exceptions.ResourceNotFoundException;
import com.hakkinenT.selfreference.tests.PersonFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

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

        Mockito.when(personRepository.findById(existingPersonId)).thenReturn(Optional.of(person));
        Mockito.when(personRepository.findById(nonExistingPersonId)).thenReturn(Optional.empty());

        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));

        Mockito.when(personRepository.getReferenceById(existingPersonId)).thenReturn(person);
        Mockito.when(personRepository.getReferenceById(nonExistingPersonId)).thenThrow(EntityNotFoundException.class);
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

    @Test
    public void findByIdShouldReturnPersonDTOWhenIdExists(){
        PersonDTO result = personService.findById(existingPersonId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingPersonId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            personService.findById(nonExistingPersonId);
        });
    }

    @Test
    public void findAllShouldReturnPersonDTOList(){
        List<PersonDTO> result = personService.findAll();
        Assertions.assertEquals(result.get(0).getId(), personDTO.getId());
        Assertions.assertEquals(result.get(0).getName(), personDTO.getName());
    }

    @Test
    public void updateShouldReturnPersonDTOWhenIdExists(){
        PersonDTO result = personService.update(existingPersonId, personDTO);

        Assertions.assertEquals(result.getId(), personDTO.getId());
        Assertions.assertEquals(result.getName(), personDTO.getName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            personService.update(nonExistingPersonId, personDTO);
        });
    }
}
