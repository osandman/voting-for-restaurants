package net.osandman.votingforrestaurants.dto;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.entity.Role;
import org.springframework.security.core.userdetails.User;

@ToString(of = "person")
public class AuthUser extends User {

    private final Person person;

    public AuthUser(@NotNull Person person) {
        super(person.getEmail(), person.getPassword(),
                person.getRoles().stream().map(Role::getType).toList());
        this.person = person;
    }

    public Integer getId() {
        return person.getId();
    }

    public Person getPerson() {
        person.setVotes(null);
        return person;
    }
}
