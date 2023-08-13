package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "person")
@ToString(callSuper = true, exclude = {"password"})
public class Person extends AbstractNamedEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 20)
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date registered;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    @NotNull
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
