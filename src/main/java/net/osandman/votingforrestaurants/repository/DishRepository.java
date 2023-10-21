package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Dish;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Override
    @Cacheable("allDishes")
    @NonNull
    List<Dish> findAll();

    @Override
    @Cacheable("dishes")
    default Dish getExisted(int id) {
        return BaseRepository.super.getExisted(id);
    }

    @Override
    @Modifying
    @Transactional
    @NonNull
    @CachePut(value = "dishes", key = "#dish.id")
    @CacheEvict(value = "allDishes", allEntries = true)
    Dish save(@NonNull Dish dish);

    @Override
    @Caching(evict = {
            @CacheEvict(value = "dishes", key = "#id"),
            @CacheEvict(value = "allDishes", allEntries = true)
    })
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}