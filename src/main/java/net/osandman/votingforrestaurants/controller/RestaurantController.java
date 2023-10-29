package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.osandman.votingforrestaurants.dto.RestaurantTo;
import net.osandman.votingforrestaurants.entity.Restaurant;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static net.osandman.votingforrestaurants.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    public static final String RESTAURANT_URL = "/restaurants";
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping(RESTAURANT_URL)
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll();
    }

    @GetMapping(RESTAURANT_URL + "/{id}")
    public Restaurant getById(@PathVariable int id) {
        log.info("getById");
        return restaurantRepository.getExisted(id);
    }

    @Transactional
    @PostMapping(value = "/admin" + RESTAURANT_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        Restaurant createdRestaurant = restaurantRepository.save(new Restaurant(restaurantTo.name(),
                restaurantTo.address()));
        URI uriOfNewRestaurant = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_URL + "/{id}")
                .build(createdRestaurant.getId());
        return ResponseEntity.created(uriOfNewRestaurant).body(createdRestaurant);
    }

    @DeleteMapping("/admin" + RESTAURANT_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        restaurantRepository.deleteExisted(id);
    }

    @Transactional
    @PutMapping(value = "/admin" + RESTAURANT_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        assureIdConsistent(restaurantTo, id);
        Restaurant updRestaurant = restaurantRepository.getExisted(id);
        updRestaurant.setName(restaurantTo.name());
        updRestaurant.setAddress(restaurantTo.address());
        restaurantRepository.save(updRestaurant);
    }
}