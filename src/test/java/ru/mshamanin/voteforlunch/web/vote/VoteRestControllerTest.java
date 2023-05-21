package ru.mshamanin.voteforlunch.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.model.Vote;
import ru.mshamanin.voteforlunch.repository.VoteRepository;
import ru.mshamanin.voteforlunch.util.ClockHolder;
import ru.mshamanin.voteforlunch.util.JsonUtil;
import ru.mshamanin.voteforlunch.web.AbstractRestControllerTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mshamanin.voteforlunch.service.VoteService.*;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.*;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.USER_EMAIL;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.USER_ID;
import static ru.mshamanin.voteforlunch.web.vote.VoteTestData.*;

public class VoteRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_URL = VoteRestController.REST_URL;
    private static final String REST_URL_WITH_SLASH = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + USER_VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + USER_VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + VOTE_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getForeignVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + ANOTHER_USER_VOTE1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVotes));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + "by-date")
                .param("date", "2023-01-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_MATCHER.contentJson(userVote2));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getByDateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_WITH_SLASH + "by-date")
                .param("date", "2023-02-01"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void createBeforeDeadline() throws Exception {
        ClockHolder.setClock(CLOCK_NEW_DAY_BEFORE_DEADLINE);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT2_ID)))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void createAfterDeadline() throws Exception {
        ClockHolder.setClock(CLOCK_NEW_DAY_AFTER_DEADLINE);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT2_ID)))
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getExisted(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void createSecondVotePerDay() throws Exception {
        ClockHolder.setClock(CLOCK_VOTE1_DAY_BEFORE_DEADLINE);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT2_ID)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(SECOND_VOTE_CREATION_ERROR)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_EMAIL)
    void updateBeforeDeadline() throws Exception {
        ClockHolder.setClock(CLOCK_VOTE1_DAY_BEFORE_DEADLINE);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + USER_VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        ;

        VOTE_MATCHER.assertMatch(voteRepository.get(USER_VOTE1_ID, USER_ID).get(), updated);
    }


    @Test
    @WithUserDetails(value = USER_EMAIL)
    void updateAfterDeadline() throws Exception {
        ClockHolder.setClock(CLOCK_VOTE1_DAY_AFTER_DEADLINE);
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + USER_VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(TOO_LATE_TO_VOTE_ERROR)));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void updateAnotherUserVote() throws Exception {
        ClockHolder.setClock(CLOCK_VOTE1_DAY_BEFORE_DEADLINE);
        Vote anotherUserVoteUpdated = anotherUserVote1;
        anotherUserVoteUpdated.setRestaurant(restaurant1);
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + ANOTHER_USER_VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(anotherUserVoteUpdated)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void updateNotTodayVote() throws Exception {
        ClockHolder.setClock(CLOCK_VOTE1_DAY_BEFORE_DEADLINE);
        Vote updated = VoteTestData.getUpdated();
        updated.setDate(userVote2.getDate());
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + USER_VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(CHANGE_NOT_TODAY_VOTE_ERROR)));
    }

    @Test
    void updateUnauth() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_WITH_SLASH + USER_VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createUnauth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT2_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void createWithRestaurantNotFound() throws Exception {
        ClockHolder.setClock(CLOCK_NEW_DAY_BEFORE_DEADLINE);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_ID_NOT_FOUND)))
                .andExpect(status().isNotFound());
    }
}