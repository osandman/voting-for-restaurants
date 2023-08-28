package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
public class MenuItem extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @NotNull
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    @NotNull
    private Dish dish;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    @Override
    public String toString() {
        return "Menu item: id=" + super.getId() + ", Dish=[" + dish.toString() + "]" +
                ", amount=" + getAmount();
    }
}
