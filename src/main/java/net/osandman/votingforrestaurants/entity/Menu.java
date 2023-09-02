package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
public class Menu extends AbstractBaseEntity {

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Vote> votes;

    @OneToMany(mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<MenuItem> menuItems;

    @Override
    public String toString() {
        return "Menu: id=" + super.getId() + ", data=" + getDate();
    }
}
