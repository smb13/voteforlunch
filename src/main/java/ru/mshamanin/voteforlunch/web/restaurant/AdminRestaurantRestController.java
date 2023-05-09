package ru.mshamanin.voteforlunch.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mshamanin.voteforlunch.model.Restaurant;

import java.net.URI;
import java.util.List;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;
import static ru.mshamanin.voteforlunch.web.restaurant.AdminRestaurantRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return super.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        super.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.update(restaurant);
    }

    @GetMapping("/by-name")
    public List<Restaurant> getByNameContaining(@RequestParam String name) {
        log.info("getByName {}", name);
        return restaurantRepository.findByNameContainingIgnoringCase(name);
    }
}