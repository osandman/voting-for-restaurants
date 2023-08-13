package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractNamedEntity {
    @Column(name = "address", unique = true, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Menu> menuList;
}
