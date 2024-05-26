package com.hakkinenT.selfreference.repositories;

import com.hakkinenT.selfreference.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p WHERE p.partner.id != null")
    List<Person> searchAllPartners();
}
