package net.osandman.votingforrestaurants.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vote")
@Getter
@Setter
public class Vote extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonBackReference
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonBackReference
    private Person person;

    @Column(name = "vote_date")
    private LocalDate voteDate;

    public Vote() {
        this.voteDate = LocalDate.now();
    }

    public Vote(Menu menu, Person person) {
        this();
        this.menu = menu;
        this.person = person;
    }

    @Override
    public String toString() {
        return "id=" + super.getId() + ", date=" + getVoteDate();
    }
}
