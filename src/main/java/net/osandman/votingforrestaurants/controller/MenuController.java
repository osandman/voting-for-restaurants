package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.MenuItemTo;
import net.osandman.votingforrestaurants.dto.MenuTo;
import net.osandman.votingforrestaurants.entity.Menu;
import net.osandman.votingforrestaurants.entity.MenuItem;
import net.osandman.votingforrestaurants.entity.Restaurant;
import net.osandman.votingforrestaurants.repository.DishRepository;
import net.osandman.votingforrestaurants.repository.MenuRepository;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.osandman.votingforrestaurants.util.DateUtil.endDayOrMax;
import static net.osandman.votingforrestaurants.util.DateUtil.startDayOrMin;
import static net.osandman.votingforrestaurants.util.ValidationUtil.assureIdConsistent;
import static net.osandman.votingforrestaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class MenuController {
    public static final String RESTAURANT_MENU_URL = "/restaurant/{restaurantId}/menus";
    public static final String MENU_URL = "/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @GetMapping(RESTAURANT_MENU_URL)
    public List<Menu> getAllByRestaurantId(@PathVariable int restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }

    @GetMapping(RESTAURANT_MENU_URL + "/with-votes")
    public List<Menu> getAllWithVotesByRestaurantId(@PathVariable int restaurantId) {
        return menuRepository.findAllWithVotesByRestaurantId(restaurantId);
    }

    @GetMapping(RESTAURANT_MENU_URL + "/{id}")
    public Menu getWithVotesByRestaurantId(@PathVariable int restaurantId, @PathVariable int id) {
        Menu menu = menuRepository.findWithItems(id);
        assureIdConsistent(menu.getRestaurant(), restaurantId);
        return menu;
    }

    @GetMapping(RESTAURANT_MENU_URL + "/with-items")
    public List<Menu> getAllWithItemsByRestaurantId(@PathVariable int restaurantId) {
        return menuRepository.findAllWithItemsByRestaurantId(restaurantId);
    }

    @GetMapping(MENU_URL)
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @GetMapping(MENU_URL + "/filter")
    public List<Menu> getAllBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return menuRepository.findAllBetweenWithMenuItems(startDayOrMin(startDate), endDayOrMax(endDate));
    }

    @GetMapping(RESTAURANT_MENU_URL + "/filter")
    public List<Menu> getAllByRestaurantBetween(@PathVariable int restaurantId,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return menuRepository.findAllByRestaurantBetweenWithMenuItems(restaurantId,
                startDayOrMin(startDate), endDayOrMax(endDate));
    }

    @PostMapping(value = "/admin" + RESTAURANT_MENU_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithItems(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        checkNew(menuTo);
        List<MenuItem> menuItems = new ArrayList<>();
        Menu createdMenu = new Menu(LocalDate.now(), menuItems);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        createdMenu.setRestaurant(restaurant);
        for (MenuItemTo itemTo : Objects.requireNonNull(menuTo.menuItems())) {
            MenuItem item = new MenuItem(dishRepository.getExisted(itemTo.id()), itemTo.amount());
            item.setMenu(createdMenu);
            menuItems.add(item);
        }
        menuRepository.save(createdMenu);
        URI uriOfNewRestaurant = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_MENU_URL + "/{id}")
                .build(restaurant.getId(), createdMenu.getId());
        return ResponseEntity.created(uriOfNewRestaurant).body(createdMenu);
    }

    @PutMapping(value = "/admin" + RESTAURANT_MENU_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        Menu updMenu = menuRepository.findWithItems(id);
        assureIdConsistent(updMenu.getRestaurant(), restaurantId);
        for (MenuItemTo itemTo : Objects.requireNonNull(menuTo.menuItems())) {
            int dishId = itemTo.id();
            MenuItem updItem = updMenu.getMenuItems().stream()
                    .filter(item -> item.getDish().id().equals(dishId))
                    .findAny()
                    .orElse(new MenuItem(dishRepository.getExisted(itemTo.id()), itemTo.amount()));
            if (updItem.isNew()) {
                updMenu.getMenuItems().add(updItem);
                updItem.setMenu(updMenu);
            } else {
                updItem.setAmount(itemTo.amount());
            }
        }
        menuRepository.save(updMenu);
    }

    @DeleteMapping("/admin" + RESTAURANT_MENU_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        assureIdConsistent(menuRepository.getExisted(id).getRestaurant(), restaurantId);
        menuRepository.deleteExisted(id);
    }
}
