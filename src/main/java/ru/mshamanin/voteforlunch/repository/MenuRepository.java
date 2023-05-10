package ru.mshamanin.voteforlunch.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

        @Query("SELECT m FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
        Optional<Menu> get(int id, int restaurantId);

        List<Menu> findByDateAndNameContainingIgnoringCaseAndRestaurantId(LocalDate date, String name, int restaurantId);

        List<Menu> findByRestaurantId(int restaurantId);

        @Cacheable("menus")
        List<Menu> findByDateAndRestaurantId(LocalDate date, int restaurantId);

        default Menu getExistedOrBelonged(int id, int restaurantId) {
                return get(id, restaurantId).orElseThrow(
                        () -> new DataConflictException("Menu id=" + id + " is not exist or doesn't belong to Restaurant id=" + restaurantId));
        }
}
