package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.PersonTo;
import net.osandman.votingforrestaurants.entity.AbstractBaseEntity;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.entity.Role;
import net.osandman.votingforrestaurants.entity.RoleType;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import net.osandman.votingforrestaurants.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AdminAccountController.ADMIN_ACCOUNT_URL)
@AllArgsConstructor
public class AdminAccountController {
    public final static String ADMIN_ACCOUNT_URL = "/admin/accounts";
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable Integer id) {
        return personRepository.findById(id).orElseThrow();
    }

    @GetMapping(value = "/{id}/with-votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getWithVotes(@PathVariable Integer id) {
        return personRepository.findPersonByIdWithVotes(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody PersonTo personTo) {
        Set<Role> roles = prepareRoles(personTo);
        Person createdPerson = personRepository.save(
                new Person(personTo.name(), personTo.email(), personTo.password(), roles.toArray(new Role[0])));
        URI uriOfNewPerson = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_ACCOUNT_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewPerson).body(createdPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        personRepository.deleteById(id);
    }

    private Set<Role> prepareRoles(PersonTo personTo) {
        Set<Role> roles;
        if (personTo.roleTypes() != null) {
            roles = personTo.roleTypes().stream()
                    .map(roleType -> roleRepository.findByType(roleType).orElse(new Role(roleType)))
                    .collect(Collectors.toSet());
            roles.stream().filter(AbstractBaseEntity::isNew).forEach(roleRepository::save);
        } else {
            roles = Set.of(roleRepository.findByType(RoleType.REGULAR)
                    .orElseGet(() -> roleRepository.save(new Role(RoleType.REGULAR))));
        }
        return roles;
    }
}
