package com.hakkinenT.selfreference.repositories;

import com.hakkinenT.selfreference.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
