package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.entity.Dish;
import net.osandman.votingforrestaurants.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static net.osandman.votingforrestaurants.util.ValidationUtil.assureIdConsistent;
import static net.osandman.votingforrestaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DishController {
    public final static String DISH_URL = "/dishes";
    private final DishRepository dishRepository;

    @GetMapping(DISH_URL)
    public List<Dish> getAll() {
        return dishRepository.findAllByOrderById();
    }

    @GetMapping(DISH_URL + "/{id}")
    public Dish get(@PathVariable int id) {
        return dishRepository.getExisted(id);
    }

    @PostMapping(value = "/admin" + DISH_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        checkNew(dish);
        Dish createdDish = dishRepository.save(dish);
        URI uriOfNewDish = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DISH_URL + "/{id}")
                .build(createdDish.getId());
        return ResponseEntity.created(uriOfNewDish).body(createdDish);
    }

    @PutMapping(value = "/admin" + DISH_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        assureIdConsistent(dish, id);
        Dish updDish = dishRepository.getExisted(id).update(dish);
        dishRepository.save(updDish);
    }

    @DeleteMapping("/admin" + DISH_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        dishRepository.deleteExisted(id);
    }
}