package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT p FROM Person p WHERE p.id=?1")
    Optional<Person> findPersonByIdWithVotes(int id);

    Optional<Person> findPersonByEmailIgnoreCase(String email);
}
