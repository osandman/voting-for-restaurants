package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractNamedEntity extends AbstractPersistable<Integer> {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 50)
    protected String name;

    @Override
    public String toString() {
        return "(id=" + super.getId() + ", name=" + this.getName() + ")";
    }
}
