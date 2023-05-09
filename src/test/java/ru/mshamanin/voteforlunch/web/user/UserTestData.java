package ru.mshamanin.voteforlunch.web.user;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Role;
import ru.mshamanin.voteforlunch.model.User;
import ru.mshamanin.voteforlunch.util.JsonUtil;

import java.util.Collections;

import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int ANOTHER_USER_ID = START_SEQ + 2;
    public static final int USER_ID_NOT_FOUND = 10;

    public static final String USER_EMAIL = "user@mail.ru";
    public static final String ADMIN_EMAIL = "admin@mail.ru";
    public static final String USER_NAME = "User";
    public static final String ADMIN_NAME = "Admin";
    public static final User user = new User(USER_ID, "User", "user@mail.ru", "password",  Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@mail.ru", "password", Role.ADMIN);
    public static final User anotherUser = new User(ANOTHER_USER_ID, "AnotherUser", "anotheruser@mail.ru", "password",  Role.USER);
    public static User getNew() {
        return new User(null, "New", "newser@mail.ru", "newPass", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setName("updatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
