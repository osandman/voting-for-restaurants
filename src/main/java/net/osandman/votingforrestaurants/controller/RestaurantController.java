package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.dto.RestaurantTo;
import net.osandman.votingforrestaurants.entity.Restaurant;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import net.osandman.votingforrestaurants.util.RestaurantUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    private final String RESTAURANT_URL = "/restaurants";
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping(RESTAURANT_URL)
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return RestaurantUtil.getTos(restaurantRepository.findAllByOrderByIdAsc());
    }

    @GetMapping(RESTAURANT_URL + "/{id}")
    public RestaurantTo getById(@PathVariable Integer id) {
        log.info("getById");
        return RestaurantUtil.getTo(restaurantRepository.findById(id).orElseThrow());
    }

    @PostMapping(value = "/admin" + RESTAURANT_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        Restaurant createdRestaurant = restaurantRepository.save(new Restaurant(restaurantTo.name(), restaurantTo.address()));
        URI uriOfNewRestaurant = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_URL + "/{id}")
                .buildAndExpand(createdRestaurant.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewRestaurant).body(createdRestaurant);
    }
}
