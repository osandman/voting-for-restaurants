package net.osandman.votingforrestaurants.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDate;

@Entity
@Table(name = "vote")
@Getter
@Setter
public class Vote extends AbstractPersistable<Integer> {

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "vote_date")
    private LocalDate voteDate;

    @Override
    public String toString() {
        return "id=" + super.getId() + ", date=" + getVoteDate();
    }
}
