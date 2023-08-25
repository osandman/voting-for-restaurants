package net.osandman.votingforrestaurants.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.dto.AuthUser;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

import static net.osandman.votingforrestaurants.entity.RoleType.ADMIN;
import static net.osandman.votingforrestaurants.entity.RoleType.REGULAR;
import static org.springframework.security.config.Customizer.withDefaults;

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
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            Optional<Person> person = personRepository.findPersonByEmailIgnoreCase(email);
            return new AuthUser(person.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/account").hasRole(REGULAR.name())
                                .requestMatchers("/api/**").hasRole(ADMIN.name())
                                .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
