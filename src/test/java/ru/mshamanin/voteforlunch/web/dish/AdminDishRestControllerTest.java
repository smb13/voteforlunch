package ru.mshamanin.voteforlunch.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.Dish;
import ru.mshamanin.voteforlunch.repository.DishRepository;
import ru.mshamanin.voteforlunch.util.JsonUtil;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.dish.DishTestData.*;
import static ru.mshamanin.voteforlunch.web.menu.MenuTestData.RESTAURANT1_MENU1_ID;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.ADMIN_EMAIL;

class AdminDishRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL_WITH_REST1_MENU1_IDS = AdminDishRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID))
            .replace("{menuId}", String.valueOf(RESTAURANT1_MENU1_ID));
    private static final String REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH = REST_URL_WITH_REST1_MENU1_IDS + "/";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(rest1Menu1Dish1));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
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
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(rest1Menu1Dishes));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByNameContaining() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + "by-name")
                .param("name", "ган"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(List.of(rest1Menu1Dish1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_WITH_REST1_MENU1_IDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.getExisted(newId), newDish);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void createDuplicate() throws Exception {
        Dish duplicateDish = new Dish(rest1Menu1Dish1);
        duplicateDish.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL_WITH_REST1_MENU1_IDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateDish)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.getExisted(RESTAURANT1_MENU1_DISH1_ID), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateDuplicate() throws Exception {
        Dish duplicateDish = new Dish(rest1Menu1Dish1);
        duplicateDish.setName(rest1Menu1Dish2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateDish)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU1_DISH1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> dishRepository.getExisted(RESTAURANT1_MENU1_DISH1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_REST1_MENU1_IDS_AND_SLASH + RESTAURANT1_MENU2_DISH1_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}