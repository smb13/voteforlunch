package ru.mshamanin.voteforlunch.web.menu;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.Menu;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.mshamanin.voteforlunch.web.menu.AdminMenuRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuRestController extends AbstractMenuController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    @GetMapping
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {} for restaurant id {}", id, restaurantId);
        return menuRepository.getExistedOrBelonged(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createMenu(@PathVariable int restaurantId, @RequestBody Menu menu) {
        log.info("create menu {} for restaurant id {}", menu, restaurantId);
        Menu created = super.create(menu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {} for restaurant id {}", id, restaurantId);
        super.delete(id, restaurantId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} with id={} for restaurant id {}", menu, id, restaurantId);
        super.update(menu, id, restaurantId);
    }

    @GetMapping("/by-date")
    public List<Menu> getByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("getByDate {} for restaurant id {}", date, restaurantId);
        return menuRepository.findByDateAndRestaurantId(date, restaurantId);
    }

    @GetMapping("/by-date-and-name")
    public List<Menu> getByDateAndNameContaining(@PathVariable int restaurantId, @RequestParam LocalDate date,
                                                 @RequestParam String name) {
        log.info("getByDateAndNameContaining date {} and name containing {} for restaurant id {}", date, name, restaurantId);
        return menuRepository.findByDateAndNameContainingIgnoringCaseAndRestaurantId(date, name, restaurantId);
    }
}