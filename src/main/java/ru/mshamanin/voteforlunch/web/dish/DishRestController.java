package ru.mshamanin.voteforlunch.web.dish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mshamanin.voteforlunch.model.Dish;
import ru.mshamanin.voteforlunch.service.DishService;

import java.util.List;

import static ru.mshamanin.voteforlunch.web.dish.DishRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dish REST-controller for all users")
public class DishRestController {
    static final String REST_URL = "/api/restaurant/{restaurantId}/menus/{menuId}/dishes";

    private DishService dishService;

    @GetMapping
    @Operation(summary = "Get all dishes for restaurant with {restaurantId} menu with {menuId}")
    public List<Dish> getAll(@PathVariable int restaurantId, @PathVariable int menuId) {
        log.info("getAll for restaurant with id {} and menu with {}", restaurantId, menuId);
        return dishService.getAll(restaurantId, menuId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dish with {id} for restaurant with {restaurantId} menu with {menuId}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int menuId, @PathVariable int id) {
        log.info("get dish with id {} for restaurant with id {} menu with {}", id, restaurantId, menuId);
        return dishService.get(restaurantId, menuId, id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get dish by part of it's name for restaurant with {restaurantId} menu with {menuId}")
    public List<Dish> getByName(@PathVariable int restaurantId, @PathVariable int menuId,
                                @RequestParam String name) {
        log.info("getNameContaining containing {} for restaurant with id {} menu with {}", name, restaurantId, menuId);
        return dishService.getByName(restaurantId, menuId, name);
    }
}