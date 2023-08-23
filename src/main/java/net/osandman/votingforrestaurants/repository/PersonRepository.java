package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT p FROM Person p JOIN FETCH p.votes WHERE p.id=?1")
    Optional<Person> findPersonByIdWithVotes(int id);

    Optional<Person> findPersonByEmailIgnoreCase(String email);
}
