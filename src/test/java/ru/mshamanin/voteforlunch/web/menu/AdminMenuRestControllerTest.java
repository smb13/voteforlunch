package ru.mshamanin.voteforlunch.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.util.JsonUtil;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.menu.MenuTestData.*;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.RESTAURANT1_ID;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.ADMIN_EMAIL;

class AdminMenuRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL_WITH_RESTAURANT1_ID = AdminMenuRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID));
    private static final String REST_URL_WITH_RESTAURANT1_ID_AND_SLASH = AdminMenuRestController.REST_URL
            .replace("{restaurantId}", String.valueOf(RESTAURANT1_ID)) + "/";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + MENU_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1Menus));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + "by-date")
                .param("date", "2023-01-30"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1MenusFor20230130));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByDateAndNameContaining() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + "by-date-and-name")
                .param("date", "2023-01-30")
                .param("name", "Греч"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(List.of(rest1Menu4)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_WITH_RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getExisted(newId), newMenu);
        List<Menu> newRest1MenusFor20230130 = new ArrayList<>(rest1MenusFor20230130);
        newRest1MenusFor20230130.add(newMenu);
        MENU_MATCHER.assertMatch(menuRepository.findByDateAndRestaurantId(LocalDate.of(2023, Month.JANUARY, 30), RESTAURANT1_ID),
                newRest1MenusFor20230130);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void createDuplicate() throws Exception {
        Menu duplicateMenu = new Menu(rest1Menu1);
        duplicateMenu.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL_WITH_RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateMenu)))
                .andExpect(status().isConflict());
    }


    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(menuRepository.getExisted(RESTAURANT1_MENU1_ID), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateDuplicate() throws Exception {
        Menu duplicateMenu = getUpdated();
        duplicateMenu.setName(rest1Menu2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateMenu)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH + RESTAURANT1_MENU1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuRepository.getExisted(RESTAURANT1_MENU1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_RESTAURANT1_ID_AND_SLASH +  RESTAURANT2_MENU1_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}