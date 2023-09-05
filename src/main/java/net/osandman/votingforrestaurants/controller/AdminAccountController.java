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

import static net.osandman.votingforrestaurants.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminAccountController.ADMIN_ACCOUNT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public Person get(@PathVariable int id) {
        return personRepository.getExisted(id);
    }

    @GetMapping("/{id}/with-votes")
    public ResponseEntity<Person> getWithVotes(@PathVariable int id) {
        return ResponseEntity.of(personRepository.findPersonByIdWithVotes(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(@Valid @RequestBody PersonTo personTo) {
        Set<Role> roles = prepareRoles(personTo, true);
        Person createdPerson = personRepository.save(
                new Person(personTo.name(), personTo.email(), personTo.password(), roles));
        URI uriOfNewPerson = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ADMIN_ACCOUNT_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewPerson).body(createdPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        personRepository.deleteExisted(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody PersonTo personTo, @PathVariable int id) {
        assureIdConsistent(personTo, id);
        Person updPerson = personRepository.getExisted(id);
        updPerson.setName(personTo.name());
        updPerson.setEmail(personTo.email());
        if (personTo.password() != null) {
            updPerson.setPassword(personTo.password());
        }
        updPerson.setRoles(prepareRoles(personTo, false));
        personRepository.save(updPerson);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setEnabled(@PathVariable int id, @RequestParam boolean enable) {
        Person patchPerson = personRepository.getExisted(id);
        patchPerson.setEnabled(enable);
        personRepository.save(patchPerson);
    }

    private Set<Role> prepareRoles(PersonTo personTo, boolean mustRegularForNewPerson) {
        Set<Role> roles = Set.of();
        if (personTo.roleTypes() != null) {
            roles = personTo.roleTypes().stream()
                    .map(roleType -> roleRepository.findByType(roleType).orElse(new Role(roleType)))
                    .collect(Collectors.toSet());
            roles.stream().filter(AbstractBaseEntity::isNew).forEach(roleRepository::save);
        } else if (mustRegularForNewPerson) {
            roles = Set.of(roleRepository.findByType(RoleType.REGULAR)
                    .orElseGet(() -> roleRepository.save(new Role(RoleType.REGULAR))));
        }
        return roles;
    }
}