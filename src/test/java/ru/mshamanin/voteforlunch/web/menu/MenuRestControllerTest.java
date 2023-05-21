package ru.mshamanin.voteforlunch.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.dish.DishTestData.rest1Menu1Dishes;
import static ru.mshamanin.voteforlunch.web.menu.MenuTestData.*;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.USER_EMAIL;

public class MenuRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL_WITH_RESTAURANT1_ID = MenuRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID));
    private static final String REST_URL_WITH_RESTAURANT1_ID_AND_SLASH = MenuRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID)) + "/";

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT2_MENU1_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1Menus));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + "by-date")
                .param("date", "2023-01-30"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1MenusFor20230130));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getWithDishes() throws Exception {
        Menu withDishes = new Menu(rest1Menu1);
        withDishes.setDishes(rest1Menu1Dishes);
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH
                + RESTAURANT1_MENU1_ID + "/with-dishes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(withDishes));
    }
}
