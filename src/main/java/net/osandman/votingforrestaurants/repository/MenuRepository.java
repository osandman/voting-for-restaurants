package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("SELECT m FROM Menu m JOIN FETCH m.menuItems WHERE m.id=?1")
    Optional<Menu> findMenuByIdWithMenuItems(int id);

    @Query("SELECT m FROM Menu m JOIN FETCH m.menuItems")
    List<Menu> findAllWithMenuItems();
}
