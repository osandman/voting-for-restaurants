package net.osandman.votingforrestaurants.entity;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Date registered = new Date();

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @JsonManagedReference
    @Nullable
    private List<Vote> votes;

    public Person(String name, String email, String password, Role... roles) {
        this(null, name, email, password, roles);
    }

    public Person(Integer id, String name, String email, String password, Role... roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = Set.of(roles);
    }

    public void setEmail(String email) {
        this.email = ObjectUtils.isEmpty(email) ? null : email.toLowerCase();
    }
}
