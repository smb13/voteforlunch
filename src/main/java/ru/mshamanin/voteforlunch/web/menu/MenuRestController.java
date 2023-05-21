package ru.mshamanin.voteforlunch.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.mshamanin.voteforlunch.web.menu.MenuRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu REST-controller for all users")
public class MenuRestController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/menus";

    private MenuRepository menuRepository;

    @GetMapping
    @Operation(summary = "Get all menu items for restaurant with {restaurantId}")
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll for restaurant id {}", restaurantId);
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item with {id} for restaurant with {restaurantId}")
    public Menu get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {} for restaurant id {}", id, restaurantId);
        return menuRepository.getExistedOrBelonged(restaurantId, id);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get menu items by date for restaurant with {restaurantId}")
    public List<Menu> getByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("getByDate {} for restaurant id {}", date, restaurantId);
        return menuRepository.findByDateAndRestaurantId(date, restaurantId);
    }

    @GetMapping("/{id}/with-dishes")
    @Operation(summary = "Get menu with {id} with it's dishes for restaurant with {restaurantId}")
    public ResponseEntity<Menu> getWithDishes(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("getWithDishes menu with id {} for restaurant id {}", id, restaurantId);
        return ResponseEntity.of(menuRepository.getWithDishes(restaurantId, id));
    }
}