package ru.mshamanin.voteforlunch.util;

import lombok.experimental.UtilityClass;
import ru.mshamanin.voteforlunch.model.Role;
import ru.mshamanin.voteforlunch.model.User;
import ru.mshamanin.voteforlunch.to.UserTo;

@UtilityClass
public final class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}