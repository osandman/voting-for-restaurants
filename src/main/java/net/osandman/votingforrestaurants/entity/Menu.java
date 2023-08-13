package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;

@Entity
@Table(name = "menu")
public class Menu extends AbstractPersistable<Integer> {

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rest_id", nullable = false)
    private Restaurant restaurant;
}
