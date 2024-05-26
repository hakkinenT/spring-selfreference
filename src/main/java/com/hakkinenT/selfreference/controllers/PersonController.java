package com.hakkinenT.selfreference.controllers;

import com.hakkinenT.selfreference.dto.PersonDTO;
import com.hakkinenT.selfreference.entities.Person;
import com.hakkinenT.selfreference.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService service;

    @PostMapping
    public ResponseEntity<PersonDTO> insert(@RequestBody PersonDTO dto){
        dto = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> findById(@PathVariable Long id){
        PersonDTO person = service.findById(id);
        return ResponseEntity.ok(person);
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll(){
        List<PersonDTO> person = service.findAll();
        return ResponseEntity.ok(person);
    }

    @GetMapping("/partners")
    public ResponseEntity<List<PersonDTO>> findAllPartners(){
        List<PersonDTO> person = service.findAllPartners();
        return ResponseEntity.ok(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> update(@PathVariable Long id, @RequestBody PersonDTO dto){
        PersonDTO person = service.update(id, dto);
        return ResponseEntity.ok(person);

    }
}
