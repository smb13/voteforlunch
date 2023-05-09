package ru.mshamanin.voteforlunch.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.User;
import ru.mshamanin.voteforlunch.repository.UserRepository;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.*;

class AdminRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL;
    private static final String REST_URL_WITH_SLASH = REST_URL + '/';

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + ADMIN_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + USER_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + "by-email?email=" + USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }


    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, anotherUser, user));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.getExisted(newId), newUser);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userRepository.getExisted(USER_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_SLASH + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userRepository.getExisted(USER_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_WITH_SLASH + USER_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL_WITH_SLASH + USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(userRepository.getExisted(USER_ID).isEnabled());
    }
}