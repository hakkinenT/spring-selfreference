package com.hakkinenT.selfreference.dto;

import com.hakkinenT.selfreference.entities.Person;

public class PersonMinDTO {
    private Long id;
    private String name;

    public PersonMinDTO() {
    }

    public PersonMinDTO(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public PersonMinDTO(Person entity) {
        name = entity.getName();
        id = entity.getId();
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
}
