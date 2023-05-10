package ru.mshamanin.voteforlunch.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.User;
import ru.mshamanin.voteforlunch.to.UserTo;
import ru.mshamanin.voteforlunch.util.UsersUtil;
import ru.mshamanin.voteforlunch.web.AuthUser;

import java.net.URI;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.user.ProfileRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User profile REST-controller")
public class ProfileRestController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    @Operation(summary = "Get authorized user profile")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return super.get(authUser.id());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete authorized user profile")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update authorized user profile")
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody @Valid UserTo userTo) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        userRepository.prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user profile")
    public ResponseEntity<User> register(@RequestBody @Valid UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = userRepository.prepareAndSave(UsersUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}