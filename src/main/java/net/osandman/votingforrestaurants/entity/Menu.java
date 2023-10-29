package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.osandman.votingforrestaurants.error.NotFoundException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MenuItem> menuItems = new ArrayList<>();

    public Menu(LocalDate date, List<MenuItem> menuItems) {
        super(null);
        this.date = date;
        this.menuItems = menuItems;
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
        item.setMenu(this);
    }

    public void removeItem(Integer id) {
        MenuItem menuItem = menuItems.stream()
                .filter(item -> item.id().equals(id))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Item with id=" + id + "not found"));
        menuItems.remove(menuItem);
    }

    @Override
    public String toString() {
        return "Menu: id=" + super.getId() + ", data=" + getDate();
    }
}
