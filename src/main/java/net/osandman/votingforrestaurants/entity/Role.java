package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "role")
@Getter
public class Role extends AbstractPersistable<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotBlank
    @Size(min = 2, max = 50)
    private RoleType type;

    @Override
    public String toString() {
        return type.name();
    }
}
