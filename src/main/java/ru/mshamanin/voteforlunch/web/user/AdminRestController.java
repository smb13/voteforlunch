package ru.mshamanin.voteforlunch.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.User;

import java.net.URI;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.user.AdminRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User REST-controller for admins")
public class AdminRestController extends AbstractUserController {
    static final String REST_URL = "/api/admin/users";

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Get user with {id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new user")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = userRepository.prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user with {id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user with {id}")
    public void update(@RequestBody @Valid User user, @PathVariable int id) {
        assureIdConsistent(user, id);
        userRepository.prepareAndSave(user);;
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get user with particular email")
    public User getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return userRepository.getExistedByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Set user with {id} enabled or disabled")
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = userRepository.getExisted(id);
        user.setEnabled(enabled);
    }
}