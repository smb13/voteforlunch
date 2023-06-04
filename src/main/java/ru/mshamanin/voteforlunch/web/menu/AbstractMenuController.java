package ru.mshamanin.voteforlunch.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.service.MenuService;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.assureIdConsistent;
import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNew;

public abstract class AbstractMenuController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuService service;
    @Autowired
    protected MenuRepository menuRepository;

    public Menu create(Menu menu, int restaurantId) {
        log.info("create {} for restaurant id {}", menu, restaurantId);
        checkNew(menu);
        return service.save(menu, restaurantId);
    }

    public void delete(int id, int restaurantId) {
        log.info("delete {} for restaurant id {}", id, restaurantId);
        Menu menu = menuRepository.getExistedOrBelonged(id, restaurantId);
        menuRepository.delete(menu);
    }

    public void update(Menu menu, int id, int restaurantId) {
        log.info("update {} with id={} for restaurant id {}", menu, id, restaurantId);
        assureIdConsistent(menu, id);
        menuRepository.getExistedOrBelonged(id, restaurantId);
        service.save(menu, restaurantId);
    }
}