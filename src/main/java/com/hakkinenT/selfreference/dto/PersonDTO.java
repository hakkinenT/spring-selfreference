package com.hakkinenT.selfreference.dto;

import com.hakkinenT.selfreference.entities.Person;

public class PersonDTO {
    private Long id;
    private String name;
    private PersonMinDTO partner;

    public PersonDTO() {
    }

    public PersonDTO(Long id, String name, PersonMinDTO partner) {
        this.id = id;
        this.name = name;
        this.partner = partner;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.partner = person.getPartner() != null ? new PersonMinDTO(person.getPartner()) : null;
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

    public PersonMinDTO getPartner() {
        return partner;
    }

    public void setPartner(PersonMinDTO partner) {
        this.partner = partner;
    }
}
