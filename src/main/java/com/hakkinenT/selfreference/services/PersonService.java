package com.hakkinenT.selfreference.services;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public PersonDTO insert(PersonDTO dto){
        Person person = new Person();
        person.setName(dto.getName());

        Person partner = new Person();
        partner.setName(dto.getPartner().getName());

        person.setPartner(partner);

        person = personRepository.save(person);

        return new PersonDTO(person);
    }

}
