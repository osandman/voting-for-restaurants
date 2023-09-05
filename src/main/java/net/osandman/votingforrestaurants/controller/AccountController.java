package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.AuthUser;
import net.osandman.votingforrestaurants.dto.PersonTo;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.entity.Role;
import net.osandman.votingforrestaurants.entity.RoleType;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import net.osandman.votingforrestaurants.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(value = AccountController.ACCOUNT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AccountController {
    public final static String ACCOUNT_URL = "/account";
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public Person getAccount(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getPerson();
    }

    @GetMapping("/with-votes")
    public ResponseEntity<Person> getWithVotes(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.of(personRepository.findPersonByIdWithVotes(authUser.getId()));
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> register(@Valid @RequestBody PersonTo personTo) {
        Role role = roleRepository.findByType(RoleType.REGULAR)
                .orElseGet(() -> new Role(RoleType.REGULAR));
        Person createdPerson = personRepository
                .save(new Person(personTo.name(), personTo.email(), personTo.password(), Set.of(role)));
        URI uriOfNewPerson = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ACCOUNT_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewPerson).body(createdPerson);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody PersonTo personTo, @AuthenticationPrincipal AuthUser authUser) {
        Person updPerson = authUser.getPerson();
        updPerson.setName(personTo.name());
        updPerson.setEmail(personTo.email());
        if (personTo.password() != null) {
            updPerson.setPassword(personTo.password());
        }
        personRepository.save(updPerson);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        personRepository.deleteExisted(authUser.getId());
    }
}