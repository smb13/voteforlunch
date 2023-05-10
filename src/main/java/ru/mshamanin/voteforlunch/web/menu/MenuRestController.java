package ru.mshamanin.voteforlunch.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mshamanin.voteforlunch.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static ru.mshamanin.voteforlunch.web.menu.MenuRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu REST-controller for all users")
public class MenuRestController extends AbstractMenuController {
    static final String REST_URL = "/api/restaurants/{restaurantId}/menus";

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
        return menuRepository.getExistedOrBelonged(id, restaurantId);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get menu items by date for restaurant with {restaurantId}")
    public List<Menu> getByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("getByDate {} for restaurant id {}", date, restaurantId);
        return  menuRepository.findByDateAndRestaurantId(date, restaurantId);
    }

    @GetMapping("/by-date-and-name")
    @Operation(summary = "Get menu items by date and part of the menu item name for restaurant with {restaurantId}")
    public List<Menu> getByDateAndNameContaining(@PathVariable int restaurantId, @RequestParam LocalDate date,
                                                 @RequestParam String name) {
        log.info("getByDateAndNameContaining date {} and name containing {} for restaurant id {}", date, name, restaurantId);
        return menuRepository.findByDateAndNameContainingIgnoringCaseAndRestaurantId(date, name, restaurantId);
    }
}