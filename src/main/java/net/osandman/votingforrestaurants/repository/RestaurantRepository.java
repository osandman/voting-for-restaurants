package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
