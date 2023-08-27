package net.osandman.votingforrestaurants.util;

import net.osandman.votingforrestaurants.dto.RestaurantTo;
import net.osandman.votingforrestaurants.entity.Restaurant;

import java.util.List;

public class RestaurantUtil {
    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantUtil::getTo).toList();
    }
}
