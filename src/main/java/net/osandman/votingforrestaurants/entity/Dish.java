package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish")
@Getter
@Setter
@NoArgsConstructor
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

    public Dish update(Dish updated) {
        this.name = updated.name;
        this.description = updated.description;
        return this;
    }
}
