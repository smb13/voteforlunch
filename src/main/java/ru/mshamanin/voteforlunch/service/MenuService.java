package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    @CacheEvict(cacheNames = "menus", key = "#menu.id")
    public Menu save(Menu menu, int restaurantId) {
        menu.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return menuRepository.save(menu);
    }

    @Transactional
    @CacheEvict(cacheNames = "menus", key = "#menu.id")
    public void delete(Menu menu) {
        menuRepository.delete(menu);
    }
}
