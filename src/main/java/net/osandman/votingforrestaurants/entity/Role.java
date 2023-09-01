package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = true)
    private RoleType type;

    @Override
    public String toString() {
        return type.name();
    }
}