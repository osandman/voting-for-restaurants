package net.osandman.votingforrestaurants.controller;

import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends RootControllerTest {
    public static final String RESTAURANT_URL = "/restaurants";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(RESTAURANT_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}