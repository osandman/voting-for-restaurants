package net.osandman.votingforrestaurants.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.dto.AuthUser;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.entity.RoleType;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Optional;

import static net.osandman.votingforrestaurants.entity.RoleType.ADMIN;
import static net.osandman.votingforrestaurants.entity.RoleType.REGULAR;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class WebSecurityConfig {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final PersonRepository personRepository;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<Person> person = personRepository.findPersonByEmailIgnoreCase(email);
            log.debug("Authenticating '{}'", person);
            return new AuthUser(person.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher("/**")
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/account").anonymous()
                                .requestMatchers("/account").authenticated()
                                .requestMatchers("/admin/**").hasRole(ADMIN.name())
                                .requestMatchers("/profile/**").hasAnyRole(ADMIN.name(), REGULAR.name())
                                .anyRequest().permitAll()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
