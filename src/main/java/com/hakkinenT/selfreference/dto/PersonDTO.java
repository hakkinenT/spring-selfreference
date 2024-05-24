package com.hakkinenT.selfreference.dto;

import com.hakkinenT.selfreference.entities.Person;

public class PersonDTO {
    private Long id;
    private String name;
    private PersonDTO partner;

    public PersonDTO() {
    }

    public PersonDTO(Long id, String name, PersonDTO partner) {
        this.id = id;
        this.name = name;
        this.partner = partner;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.partner = person.getPartner() != null ? new PersonDTO(person.getPartner()) : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonDTO getPartner() {
        return partner;
    }

    public void setPartner(PersonDTO partner) {
        this.partner = partner;
    }
}
