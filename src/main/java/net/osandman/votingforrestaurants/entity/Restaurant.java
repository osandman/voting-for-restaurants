package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "address", unique = true, nullable = false)
    @NotBlank
    @Size(max = 100)
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private List<Menu> menuList;

    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
    }
}