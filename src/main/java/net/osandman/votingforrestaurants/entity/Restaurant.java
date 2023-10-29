package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@ToString(callSuper = true, exclude = "menuList")
@NoArgsConstructor
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Menu> menuList;

    public Restaurant(String name, String address) {
        this(null, name, address);
    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }
}
