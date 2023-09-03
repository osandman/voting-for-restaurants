package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    public List<Dish> findAllByOrderById();
}
