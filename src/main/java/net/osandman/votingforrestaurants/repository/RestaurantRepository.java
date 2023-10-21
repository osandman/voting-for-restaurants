package net.osandman.votingforrestaurants.repository;

import net.osandman.votingforrestaurants.entity.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Cacheable("restaurants")
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menuList WHERE r.id=?1")
    Optional<Restaurant> findRestaurantWithMenu(int id);

    @Override
    @Cacheable("restaurants")
    default Restaurant getExisted(int id) {
        return BaseRepository.super.getExisted(id);
    }

    @Override
    @Cacheable("allRestaurants")
    @NonNull
    List<Restaurant> findAll();

    @Override
    @Modifying
    @Transactional
    @NonNull
    @CachePut(value = "restaurants", key = "#restaurant.id")
    @CacheEvict(value = "allRestaurants", allEntries = true)
    Restaurant save(@NonNull Restaurant restaurant);

    @Override
    @Caching(evict = {
            @CacheEvict(value = "restaurants", key = "#id"),
            @CacheEvict(value = "allRestaurants", allEntries = true)
    })
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}