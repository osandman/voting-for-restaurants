package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
