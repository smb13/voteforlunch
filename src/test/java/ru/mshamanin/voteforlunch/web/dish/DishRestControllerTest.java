package ru.mshamanin.voteforlunch.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.dish.DishTestData.*;
import static ru.mshamanin.voteforlunch.web.menu.MenuTestData.RESTAURANT1_MENU1_ID;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.USER_EMAIL;

public class DishRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL_WITH_REST1_MENU1_IDS = DishRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID))
            .replace("{menuId}", String.valueOf(RESTAURANT1_MENU1_ID));
    private static final String REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH = REST_URL_WITH_REST1_MENU1_IDS + "/";

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(rest1Menu1Dish1));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU2_DISH1_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(rest1Menu1Dishes));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getByNameContaining() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + "by-name")
                .param("name", "ган"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(List.of(rest1Menu1Dish1)));
    }
}
