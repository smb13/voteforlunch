package ru.mshamanin.voteforlunch.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.mshamanin.voteforlunch.model.Restaurant;

import java.util.List;
import java.util.Optional;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNotFoundWithId;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    default List<Restaurant> getAll() {
        return findAll(SORT_NAME);
    }

    List<Restaurant> findByNameContainingIgnoringCase(String name);

    @Transactional
    @Modifying
    default Restaurant create(Restaurant restaurant){
        Assert.notNull(restaurant, " restaurant must not be null");
        return save(restaurant);
    }

    @Transactional
    @Modifying
    default void update(Restaurant restaurant) {
        Assert.notNull(restaurant, " restaurant must not be null");
        checkNotFoundWithId(save(restaurant), restaurant.id());
    }
}
