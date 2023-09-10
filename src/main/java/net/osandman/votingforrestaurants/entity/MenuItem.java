package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item")
@Getter
@Setter
@NoArgsConstructor
public class MenuItem extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @NotNull
    @JsonBackReference
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    @NotNull
    private Dish dish;

    @Column(name = "amount")
    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    public MenuItem(Dish dish, BigDecimal amount) {
        this.dish = dish;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Menu item: id=" + super.getId() + ", Dish=[" + dish.toString() + "]" +
                ", amount=" + getAmount();
    }
}
