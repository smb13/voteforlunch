package ru.mshamanin.voteforlunch.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.*;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.USER_EMAIL;

class RestaurantRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL;
    private static final String REST_URL_WITH_SLASH = REST_URL + '/';

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + RESTAURANT_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getByNameContaining() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + "by-name?name=" + pieceOfRestaurantsNames))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant3, restaurant2));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant3, restaurant1, restaurant2));
    }
}