package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "person")
@ToString(callSuper = true, exclude = {"password", "votes"})
@Getter
@Setter
@NoArgsConstructor
public class Person extends AbstractNamedEntity implements Serializable {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date registered = new Date();

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles;

    public Set<RoleType> getRoleTypes() {
        return roles.stream().map(Role::getType).collect(Collectors.toSet());
    }

    @OneToMany(mappedBy = "person")
    @JsonManagedReference
    @Nullable
    private List<Vote> votes;

    public Person(String name, String email, String password, Set<Role> roles) {
        this(null, name, email, password, roles);
    }

    public Person(Integer id, String name, String email, String password, Set<Role> roles) {
        super(id, name);
        setEmail(email);
        this.password = password;
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = ObjectUtils.isEmpty(email) ? null : email.toLowerCase();
    }
}
