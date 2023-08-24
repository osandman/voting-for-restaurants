package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
public class MenuItem extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Override
    public String toString() {
        return "Menu item: id=" + super.getId() + ", Dish=[" + dish.toString() + "]" +
                ", price=" + getPrice();
    }
}
