package ru.mshamanin.voteforlunch.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.error.DataConflictException;
import ru.mshamanin.voteforlunch.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.menu.id=:menuId")
    Optional<Dish> get(int menuId, int id);

    List<Dish> findByMenuId(int menuId);

    default Dish getExistedOrBelonged(int menuId, int id) {
        return get(menuId, id).orElseThrow(
                () -> new DataConflictException(
                        "Dish id=" + id + " is not exist or doesn't belong to menu id=" + menuId
                ));
    }

    List<Dish> findByMenuIdAndNameContainingIgnoringCase(int menuId, String name);
}
