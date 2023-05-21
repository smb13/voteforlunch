package ru.mshamanin.voteforlunch.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.Restaurant;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.restaurant.AdminRestaurantRestController.REST_URL;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Restaurant REST-controller for admins")
public class AdminRestaurantRestController {
    static final String REST_URL = "/api/admin/restaurants";

    private RestaurantRepository restaurantRepository;

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
        return restaurantRepository.getExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new restaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant with {id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update restaurant with {id}")
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.update(restaurant);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Get restaurants that contains name as a part of restaurant name")
    public List<Restaurant> getByNameContaining(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantRepository.findByNameContainingIgnoringCase(name);
    }
}