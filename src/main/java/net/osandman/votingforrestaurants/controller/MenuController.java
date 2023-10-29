package net.osandman.votingforrestaurants.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.dto.MenuItemTo;
import net.osandman.votingforrestaurants.dto.MenuTo;
import net.osandman.votingforrestaurants.entity.Dish;
import net.osandman.votingforrestaurants.entity.Menu;
import net.osandman.votingforrestaurants.entity.MenuItem;
import net.osandman.votingforrestaurants.entity.Restaurant;
import net.osandman.votingforrestaurants.error.IllegalRequestDataException;
import net.osandman.votingforrestaurants.error.NotFoundException;
import net.osandman.votingforrestaurants.repository.DishRepository;
import net.osandman.votingforrestaurants.repository.MenuRepository;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @GetMapping(RESTAURANT_MENU_URL + "/{id}")
    public Menu getWithItemsByMenuId(@PathVariable int restaurantId, @PathVariable int id) {
        Menu menu = menuRepository.findWithItems(id);
        if (!Objects.requireNonNull(menu.getRestaurant().getId()).equals(restaurantId)) {
            throw new NotFoundException("Restaurant must has id=" + restaurantId);
        }
        return menu;
    }

    @GetMapping(RESTAURANT_MENU_URL + "/with-items")
    public List<Menu> getAllWithItemsByRestaurantId(@PathVariable int restaurantId) {
        return menuRepository.findAllWithItemsByRestaurantId(restaurantId);
    }

    @GetMapping(MENU_URL + "/filter")
    public Map<Map<String, Integer>, List<Menu>> getAllBetween(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Menu> allBetweenWithMenuItems = menuRepository.findAllBetweenWithMenuItems(startDayOrMin(startDate), endDayOrMax(endDate));
        return allBetweenWithMenuItems.stream()
                .collect(Collectors.groupingBy(menu ->
                        Map.of("restaurantId", Objects.requireNonNull(menu.getRestaurant().getId()))));
    }

    @GetMapping(RESTAURANT_MENU_URL + "/with-items/filter")
    public List<Menu> getAllByRestaurantBetween(@PathVariable int restaurantId,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return menuRepository.findAllByRestaurantBetweenWithMenuItems(restaurantId,
                startDayOrMin(startDate), endDayOrMax(endDate));
    }

    @Transactional
    @PostMapping(value = "/admin" + RESTAURANT_MENU_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithItems(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        checkNew(menuTo);
        if (Objects.requireNonNull(menuTo.date()).isBefore(LocalDate.now())) {
            throw new IllegalRequestDataException("Date must be before current day");
        }
        List<MenuItem> menuItems = new ArrayList<>();
        Menu createdMenu = new Menu(menuTo.date(), menuItems);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        createdMenu.setRestaurant(restaurant);
        for (MenuItemTo itemTo : Objects.requireNonNull(menuTo.menuItems())) {
            MenuItem item = new MenuItem(dishRepository.getExisted(itemTo.dishId()), itemTo.amount());
            createdMenu.addItem(item);
        }
        menuRepository.save(createdMenu);
        URI uriOfNewRestaurant = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RESTAURANT_MENU_URL + "/{id}")
                .build(restaurant.getId(), createdMenu.getId());
        return ResponseEntity.created(uriOfNewRestaurant).body(createdMenu);
    }

    @Transactional
    @PutMapping(value = "/admin" + RESTAURANT_MENU_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        Menu updMenu = menuRepository.findMenuByIdWithMenuItems(id).orElse(menuRepository.getExisted(id));
        if (updMenu == null || menuTo.menuItems() == null) {
            return;
        }
        if (!Objects.requireNonNull(updMenu.getRestaurant().getId()).equals(restaurantId)) {
            throw new NotFoundException("Restaurant must has id=" + restaurantId);
        }
        List<Integer> dishIds = menuTo.menuItems().stream().map(MenuItemTo::dishId).toList();
        Map<Integer, Dish> dishes = dishRepository.findAllByIdIn(dishIds).stream()
                .collect(Collectors.toMap(Dish::getId, Function.identity()));
        updateMenu(menuTo, updMenu, dishes);
        menuRepository.save(updMenu);
    }

    private static void updateMenu(MenuTo menuTo, Menu updMenu, Map<Integer, Dish> dishes) {
        List<MenuItem> updatedItems = new ArrayList<>();
        for (MenuItemTo itemTo : menuTo.menuItems()) {
            int dishId = itemTo.dishId();
            MenuItem updItem = updMenu.getMenuItems().stream()
                    .filter(item -> item.getDish().id().equals(dishId))
                    .findAny()
                    .orElse(new MenuItem(dishes.get(dishId), itemTo.amount()));
            updatedItems.add(updItem);
            if (updItem.isNew()) {
                updMenu.addItem(updItem);
            } else {
                updItem.setAmount(itemTo.amount());
            }
        }
        updMenu.getMenuItems().retainAll(updatedItems);
    }

    @Transactional
    @DeleteMapping("/admin" + RESTAURANT_MENU_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        assureIdConsistent(menuRepository.getExisted(id).getRestaurant(), restaurantId);
        menuRepository.deleteExisted(id);
    }
}
