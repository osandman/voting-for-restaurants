package net.osandman.votingforrestaurants.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.entity.Role;
import org.springframework.security.core.userdetails.User;

@Getter
@ToString(of = "person")
public class AuthUser extends User {

    @JsonIgnore
    private final Person person;

    public AuthUser(@NotNull Person person) {
        super(person.getEmail(), person.getPassword(),
                person.getRoles().stream().map(Role::getType).toList());
        this.person = person;
    }
}
