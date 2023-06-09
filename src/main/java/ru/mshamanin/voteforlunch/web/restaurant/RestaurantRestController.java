package ru.mshamanin.voteforlunch.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mshamanin.voteforlunch.model.Restaurant;

import java.util.List;

import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant REST-controller for all users")
public class RestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @GetMapping
    @Operation(summary = "Get all restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant with {id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return super.get(id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get restaurants that contains name as a part of restaurant name")
    public List<Restaurant> getByNameContaining(@RequestParam String name) {
        log.info("getByNameContaining {}", name);
        return restaurantRepository.findByNameContainingIgnoringCase(name);
    }
}