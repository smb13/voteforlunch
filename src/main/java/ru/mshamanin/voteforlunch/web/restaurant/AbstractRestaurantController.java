package ru.mshamanin.voteforlunch.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mshamanin.voteforlunch.model.Restaurant;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;

public abstract class AbstractRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RestaurantRepository restaurantRepository;

    public Restaurant get(int id) {
        log.info("get {}", id);
        return restaurantRepository.getExisted(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    public void update(Restaurant restaurant, int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.update(restaurant);
    }
}