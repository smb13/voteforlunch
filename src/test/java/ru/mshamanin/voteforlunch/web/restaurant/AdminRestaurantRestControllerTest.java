package ru.mshamanin.voteforlunch.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.Restaurant;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;
import ru.mshamanin.voteforlunch.util.JsonUtil;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.*;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.ADMIN_EMAIL;

class AdminRestaurantRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = AdminRestaurantRestController.REST_URL;
    private static final String REST_URL_WITH_SLASH = REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
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
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByNameContaining() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + "by-name?name=" + pieceOfRestaurantsNames))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant3, restaurant2));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant3, restaurant1, restaurant2));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(newId), newRestaurant);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void createDuplicate() throws Exception {
        Restaurant duplicateRestaurant = new Restaurant(null, restaurant1.getName());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateRestaurant)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getExisted(RESTAURANT1_ID), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateDuplicate() throws Exception {
        Restaurant duplicateRestaurant = new Restaurant(restaurant1);
        duplicateRestaurant.setName(restaurant2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateRestaurant)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_SLASH + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantRepository.getExisted(RESTAURANT1_ID));
        assertEquals(0, menuRepository.findByRestaurantId(RESTAURANT1_ID).size());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_SLASH + RESTAURANT_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}