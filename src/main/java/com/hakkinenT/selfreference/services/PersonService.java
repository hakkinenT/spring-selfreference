package com.hakkinenT.selfreference.services;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.repositories.PersonRepository;
import com.hakkinenT.selfreference.services.exceptions.DatabaseException;
import com.hakkinenT.selfreference.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public PersonDTO insert(PersonDTO dto){
        Person person = new Person();
        person.setName(dto.getName());

        Person partner = new Person();
        partner.setName(dto.getPartner().getName());

        person.setPartner(partner);

        person = personRepository.save(person);

        return new PersonDTO(person);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!personRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso " + id + " não encontrado!");
        }

        try{
            personRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Violação de Integridade. O recurso " + id + " possui relação com outro recurso.");
        }
    }

    @Transactional(readOnly = true)
    public PersonDTO findById(Long id){
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso " + id + "não encontrado!"));
        return new PersonDTO(person);
    }

    @Transactional(readOnly = true)
    public List<PersonDTO> findAll(){
        List<Person> person = personRepository.findAll();
        return person.stream().map(PersonDTO::new).toList();
    }

    @Transactional
    public PersonDTO update(Long id, PersonDTO dto){
        try{
            Person person = personRepository.getReferenceById(id);
            person.setName(dto.getName());

            Person partner = new Person();
            partner.setName(dto.getPartner().getName());
            person.setPartner(partner);

            person = personRepository.save(person);
            return new PersonDTO(person);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Resource " + id + " não encontrado!");
        }
    }
}
