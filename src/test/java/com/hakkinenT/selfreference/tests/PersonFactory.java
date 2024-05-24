package com.hakkinenT.selfreference.tests;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;

public class PersonFactory {
    public static Person createPerson(String name){
        Person person = new Person();
        person.setId(1L);
        person.setName(name);
        return person;
    }

    public static Person createPerson(String name, String partnerName){
        Person partner = createPartner(partnerName);
        Person person = createPerson(name);
        person.setPartner(partner);

        return person;
    }

    private static Person createPartner(String name){
        Person partner = createPerson(name);
        partner.setId(2L);

        return partner;
    }

    public static PersonDTO createPersonDTO(String name){
        Person person = createPerson(name);
        return new PersonDTO(person);
    }

    public static PersonDTO createPersonDTO(String name, String partnerName){
        Person person = createPerson(name, partnerName);
        return new PersonDTO(person);
    }

}
