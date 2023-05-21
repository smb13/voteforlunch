package ru.mshamanin.voteforlunch.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.Dish;
import ru.mshamanin.voteforlunch.repository.DishRepository;
import ru.mshamanin.voteforlunch.service.DishService;

import java.net.URI;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.dish.AdminDishRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dish REST-controller for admins")
public class AdminDishRestController {

    public static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus/{menuId}/dishes";

    private DishService dishService;
    private DishRepository dishRepository;

    @GetMapping
    @Operation(summary = "Get all dishes for restaurant with {restaurantId} menu with {menuId}")
    public List<Dish> getAll(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("getAll for restaurant with id {} and menu with id {}", restaurantId, menuId);
        return dishService.getAll(restaurantId, menuId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dish with id {id} for restaurant with {restaurantId} menu with {menuId}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("get with id={} for restaurant with id {} menu with id {}", id, restaurantId, menuId);
        return dishService.get(restaurantId, menuId, id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get dish by part of it's name for restaurant with {restaurantId} menu with {menuId}")
    public List<Dish> getByName(@PathVariable int restaurantId, @PathVariable int menuId,
                                @RequestParam String name) {
        log.info("getNameContaining name containing {} for restaurant with id {} menu with id {}", name, restaurantId, menuId);
        return dishService.getByName(restaurantId, menuId, name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new dish item for restaurant with {restaurantId} menu with {menuId}")
    public ResponseEntity<Dish> createDish(@PathVariable int restaurantId, @PathVariable int menuId, @RequestBody Dish dish) {
        log.info("create {} for restaurant with id {} menu with id {}", dish, restaurantId, menuId);
        checkNew(dish);
        Dish created = dishService.save(dish, restaurantId, menuId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, menuId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dish with {id} for restaurant with {restaurantId} menu with {menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("delete with id={} for restaurant with id {} menu with {}", id, restaurantId, menuId);
        dishService.delete(restaurantId, menuId, id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update dish with {id} for restaurant with {restaurantId} menu with {menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("update {} with id={} for restaurant with id {} menu with id {}", dish, id, restaurantId, menuId);
        assureIdConsistent(dish, id);
        dishRepository.getExistedOrBelonged(menuId, id);
        dishService.save(dish, restaurantId, menuId);
    }
}