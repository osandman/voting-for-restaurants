package net.osandman.votingforrestaurants.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import net.osandman.votingforrestaurants.dto.AuthUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final PersonRepository personRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            Optional<Person> person = personRepository.findPersonByEmailIgnoreCase(email);
            return new AuthUser(person.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }
}
