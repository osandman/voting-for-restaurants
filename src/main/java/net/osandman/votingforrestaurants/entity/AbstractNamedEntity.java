package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 100)
    protected String name;

    @Override
    public String toString() {
        return "(id=" + super.getId() + ", name=" + this.getName() + ")";
    }
}
