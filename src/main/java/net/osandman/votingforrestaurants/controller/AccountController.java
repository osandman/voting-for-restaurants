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

@RestController
@RequestMapping(AccountController.ACCOUNT_URL)
@AllArgsConstructor
public class AccountController {
    public final static String ACCOUNT_URL = "/account";
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getAccount(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getPerson();
    }

    @GetMapping(value = "/with-votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getWithVotes(@AuthenticationPrincipal AuthUser authUser) {
        return personRepository.findPersonByIdWithVotes(authUser.getId()).orElseThrow();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> register(@Valid @RequestBody PersonTo personTo) {
        Role role = roleRepository.queryByType(RoleType.REGULAR).orElse(new Role(RoleType.REGULAR));
        if (role.isNew()) {
            roleRepository.save(role);
        }
        Person createdPerson = personRepository
                .save(new Person(personTo.name(), personTo.email(), personTo.password(), role));
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
        personRepository.deleteById(authUser.getId());
    }
}
