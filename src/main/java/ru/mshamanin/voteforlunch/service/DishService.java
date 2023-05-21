package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.model.Dish;
import ru.mshamanin.voteforlunch.repository.DishRepository;
import ru.mshamanin.voteforlunch.repository.MenuRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class DishService {
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;

    public List<Dish> getAll(int restaurantId, int menuId) {
        menuRepository.getExistedOrBelonged(restaurantId, menuId);
        return dishRepository.findByMenuId(menuId);
    }

    public Dish get(int restaurantId, int menuId, int id) {
        menuRepository.getExistedOrBelonged(restaurantId, menuId);
        return dishRepository.getExistedOrBelonged(menuId, id);
    }

    public List<Dish> getByName(int restaurantId, int menuId, String name) {
        menuRepository.getExistedOrBelonged(restaurantId, menuId);
        return dishRepository.findByMenuIdAndNameContainingIgnoringCase(menuId, name);
    }

    @Transactional
    @CacheEvict(cacheNames = "menus", key = "#menuId")
    public Dish save(Dish dish, int restaurantId, int menuId) {
        dish.setMenu(menuRepository.getExistedOrBelonged(restaurantId, menuId));
        return dishRepository.save(dish);
    }

    @Transactional
    @CacheEvict(cacheNames = "menus", key = "#menuId")
    public void delete(int restaurantId, int menuId, int id) {
        menuRepository.getExistedOrBelonged(restaurantId, menuId);
        Dish dish = dishRepository.getExistedOrBelonged(menuId, id);
        dishRepository.delete(dish);
    }
}
