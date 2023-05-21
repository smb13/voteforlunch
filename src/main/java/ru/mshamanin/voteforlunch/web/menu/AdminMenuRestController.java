package ru.mshamanin.voteforlunch.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.service.MenuService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.menu.AdminMenuRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Menu REST-controller for admins")
public class AdminMenuRestController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    private MenuService menuService;
    private MenuRepository menuRepository;

    @GetMapping
    @Operation(summary = "Get all menu items for restaurant with {restaurantId}")
    public List<Menu> getAll(@PathVariable int restaurantId) {
        log.info("getAll");
        return menuRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu with {id} for restaurant with {restaurantId}")
    public Menu get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get {} for restaurant id {}", id, restaurantId);
        return menuRepository.getExistedOrBelonged(restaurantId, id);
    }

    @GetMapping("/{id}/with-dishes")
    @Operation(summary = "Get menu with {id} with it's dishes for restaurant with {restaurantId}")
    public ResponseEntity<Menu> getWithDishes(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("getWithDishes menu with id {} for restaurant id {}", id, restaurantId);
        return ResponseEntity.of(menuRepository.getWithDishes(restaurantId, id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new menu item for restaurant with {restaurantId}")
    public ResponseEntity<Menu> createMenu(@PathVariable int restaurantId, @RequestBody Menu menu) {
        checkNew(menu);
        Menu created = menuService.save(menu, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item with {id} for restaurant with {restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete {} for restaurant id {}", id, restaurantId);
        Menu menu = menuRepository.getExistedOrBelonged(restaurantId, id);
        menuRepository.delete(menu);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update menu item with {id} for restaurant with {restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} with id={} for restaurant id {}", menu, id, restaurantId);
        assureIdConsistent(menu, id);
        menuRepository.getExistedOrBelonged(restaurantId, id);
        menuService.save(menu, restaurantId);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get menu items by date for restaurant with {restaurantId}")
    public List<Menu> getByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        log.info("getByDate {} for restaurant id {}", date, restaurantId);
        return menuRepository.findByDateAndRestaurantId(date, restaurantId);
    }
}