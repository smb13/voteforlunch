package ru.mshamanin.voteforlunch.web;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.mshamanin.voteforlunch.model.Role;
import ru.mshamanin.voteforlunch.model.User;

import static java.util.Objects.requireNonNull;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getName(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }

    public static AuthUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return (auth.getPrincipal() instanceof AuthUser au) ? au : null;
    }

    public static AuthUser get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    public static int authId() {
        return get().id();
    }

    @Override
    public String toString() {
        return "AuthUser:" + user.getId() + '[' + user.getName() + ']';
    }
}