package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dish")
@Getter
@Setter
public class Dish extends AbstractNamedEntity {
    @Override
    public String toString() {
        return "id=" + super.getId() + ", descr=" + super.name;
    }
}
