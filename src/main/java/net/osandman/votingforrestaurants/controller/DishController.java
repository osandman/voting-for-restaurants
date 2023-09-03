package net.osandman.votingforrestaurants.controller;

import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.entity.Dish;
import net.osandman.votingforrestaurants.repository.DishRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = DishController.DISH_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DishController {
    public final static String DISH_URL = "/dishes";
    private final DishRepository dishRepository;

    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAllByOrderById();
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable Integer id) {
        return dishRepository.getExisted(id);
    }
}
