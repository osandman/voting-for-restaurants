package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.entity.Dish;
import net.osandman.votingforrestaurants.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static net.osandman.votingforrestaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DishController {
    public static final String DISH_URL = "/dishes";
    private final DishRepository dishRepository;

    @GetMapping(DISH_URL)
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @GetMapping(DISH_URL + "/{id}")
    public Dish get(@PathVariable int id) {
        return dishRepository.getExisted(id);
    }

    @Transactional
    @PostMapping(value = "/admin" + DISH_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        checkNew(dish);
        Dish createdDish = dishRepository.save(dish);
        URI uriOfNewDish = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(DISH_URL + "/{id}")
                .build(createdDish.getId());
        return ResponseEntity.created(uriOfNewDish).body(createdDish);
    }

    @DeleteMapping("/admin" + DISH_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        dishRepository.deleteExisted(id);
    }
}