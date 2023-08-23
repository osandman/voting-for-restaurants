package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "role")
public class Role extends AbstractPersistable<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    @NotBlank
    @Size(min = 2, max = 50)
    private RoleType roleType;

    @Override
    public String toString() {
        return super.getId() + " " + roleType;
    }
}
