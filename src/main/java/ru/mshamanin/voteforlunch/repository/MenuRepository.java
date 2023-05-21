package ru.mshamanin.voteforlunch.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ProblemDetail;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Optional<Menu> get(int restaurantId, int id);

    @Cacheable(cacheNames = "menus", key = "#id")
    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.dishes WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    Optional<Menu> getWithDishes(int restaurantId, int id);

    List<Menu> findByRestaurantId(int restaurantId);

    List<Menu> findByDateAndRestaurantId(LocalDate date, int restaurantId);

    default Menu getExistedOrBelonged(int restaurantId, int id) {
        return get(restaurantId, id).orElseThrow(
                () -> new DataConflictException(
                        "Menu id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId
                ));
    }
}
