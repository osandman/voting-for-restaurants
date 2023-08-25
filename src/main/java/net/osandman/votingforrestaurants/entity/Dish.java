package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "dish")
@Getter
@Setter
public class Dish extends AbstractNamedEntity {

    @Column(name = "description", nullable = false)
    @NotNull
    @Size(max = 500)
    private String description;

    @Override
    public String toString() {
        return "id=" + super.getId() +
                ", name=" + super.name +
                ", descr=" + description;
    }
}
