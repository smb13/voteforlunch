package ru.mshamanin.voteforlunch.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.error.NotFoundException;
import ru.mshamanin.voteforlunch.model.Vote;
import ru.mshamanin.voteforlunch.repository.VoteRepository;
import ru.mshamanin.voteforlunch.service.VoteService;
import ru.mshamanin.voteforlunch.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vote REST-controller for all users")
public class VoteRestController {
    static final String REST_URL = "/api/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteService voteService;
    private final VoteRepository voteRepository;

    public VoteRestController(VoteService voteService, VoteRepository voteRepository) {
        this.voteService = voteService;
        this.voteRepository = voteRepository;
    }

    @GetMapping
    @Operation(summary = "Get all votes of authorized user")
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("getAll for user {}", userId);
        return voteRepository.getAll(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vote with {id} of authorized user")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("get vote by id {} for user {}", id, userId);
        return ResponseEntity.of(voteRepository.get(id, authUser.id()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create vote by authorized user for restaurant with restaurantId")
    public ResponseEntity<Vote> createVote(@AuthenticationPrincipal AuthUser authUser, @RequestBody int restaurantId) {
        int userId = authUser.id();
        log.info("create vote for restaurant id {} for user id {}", restaurantId, userId);
        Vote created = voteService.create(restaurantId, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update vote by authorized user for restaurant with restaurantId")
    public void update(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestBody Vote vote) {
        int userId = authUser.id();
        log.info("update vote {} for user id {}", vote, userId);
        assureIdConsistent(vote, id);
        voteRepository.getExistedOrBelonged(userId, id);
        voteService.update(vote);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get vote of authorized user for particular date")
    public Vote getByDate(@AuthenticationPrincipal AuthUser authUser, @RequestParam LocalDate date) {
        int userId = authUser.id();
        return voteRepository.findByDate(date, userId).orElseThrow(() -> new NotFoundException("Vote with date=" + date + " for user id=" + userId + " not found"));
    }
}
