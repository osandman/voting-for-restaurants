package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menuList WHERE r.id=?1")
    Optional<Restaurant> findRestaurantWithMenu(int id);

    List<Restaurant> findAllByOrderByIdAsc();
}
