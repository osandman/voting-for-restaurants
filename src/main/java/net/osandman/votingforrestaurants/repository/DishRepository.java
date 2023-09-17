package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Dish;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Cacheable("dishes")
    List<Dish> findAllByOrderById();

    @Override
    @Cacheable("dishes")
    default Dish getExisted(int id) {
        return BaseRepository.super.getExisted(id);
    }

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "dishes", key = "#dish.id")
    Dish save(Dish dish);

    @Override
    @CacheEvict(value = "dishes", allEntries = true)
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}
