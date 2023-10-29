package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Menu;
import net.osandman.votingforrestaurants.error.NotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m FROM Menu m JOIN FETCH m.menuItems WHERE m.id=?1")
    Optional<Menu> findMenuByIdWithMenuItems(int id);

    Optional<Menu> findMenuById(Integer id);

    List<Menu> findAllByRestaurantId(int restaurantId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.votes WHERE m.restaurant.id = :restaurantId")
    List<Menu> findAllWithVotesByRestaurantId(int restaurantId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.menuItems WHERE m.restaurant.id = :restaurantId")
    List<Menu> findAllWithItemsByRestaurantId(int restaurantId);

    @Query("""
            SELECT m FROM Menu m JOIN FETCH m.menuItems
            WHERE m.date >= :startDate AND m.date <= :endDate
            ORDER BY m.date DESC
            """)
    List<Menu> findAllBetweenWithMenuItems(LocalDate startDate, LocalDate endDate);
    @Query("""
            SELECT m FROM Menu m JOIN FETCH m.menuItems
            WHERE m.restaurant.id = :restaurantId AND m.date >= :startDate AND m.date <= :endDate
            ORDER BY m.date DESC
            """)
    List<Menu> findAllByRestaurantBetweenWithMenuItems(int restaurantId, LocalDate startDate, LocalDate endDate);

    Optional<Menu> findMenuByRestaurantIdAndDate(int restaurantId, LocalDate date);

    default Menu findWithItems(int id) {
        return findMenuByIdWithMenuItems(id)
                .orElseThrow(() -> new  NotFoundException("Not found items from menu with id=" + id));
    }
}
