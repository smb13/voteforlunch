package ru.mshamanin.voteforlunch.web.vote;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Vote;

import java.time.*;
import java.util.List;

import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant1;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant2;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.anotherUser;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user");

    public static final int USER_VOTE1_ID = START_SEQ + 24;
    public static final int ANOTHER_USER_VOTE1_ID = START_SEQ + 27;
    public static final int VOTE_ID_NOT_FOUND = START_SEQ + 40;

    public static final Clock clockNewDayBeforeDeadLine = Clock.fixed(Instant.parse("2023-02-01T10:00:00Z"), ZoneOffset.UTC);
    public static final Clock clockNewDayAfterDeadline = Clock.fixed(Instant.parse("2023-02-01T12:00:00Z"), ZoneOffset.UTC);
    public static final Clock clockVote1DayBeforeDeadline = Clock.fixed(Instant.parse("2023-01-29T10:00:00Z"), ZoneOffset.UTC);
    public static final Clock clockVote1DayAfterDeadline = Clock.fixed(Instant.parse("2023-01-29T12:00:00Z"), ZoneOffset.UTC);
    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, LocalDate.of(2023, Month.JANUARY, 29), restaurant1, user);
    public static final Vote userVote2 = new Vote(USER_VOTE1_ID + 1, LocalDate.of(2023, Month.JANUARY, 30), restaurant2, user);
    public static final Vote userVote3 = new Vote(USER_VOTE1_ID + 2, LocalDate.of(2023, Month.JANUARY, 31), restaurant1, user);
    public static final Vote anotherUserVote1 = new Vote(ANOTHER_USER_VOTE1_ID, LocalDate.of(2023, Month.JANUARY, 29), restaurant2, anotherUser);

    public static final List<Vote> userVotes = List.of(userVote1, userVote2, userVote3);
    public static final Vote newVote = new Vote(null, LocalDate.of(2023, Month.FEBRUARY, 01), restaurant2, user);

    public static Vote getUpdated() {
        Vote updated = new Vote(userVote1);
        updated.setRestaurant(restaurant2);
        return updated;
    }
}
