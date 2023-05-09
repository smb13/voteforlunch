package ru.mshamanin.voteforlunch.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.repository.MenuRepository;
import ru.mshamanin.voteforlunch.repository.RestaurantRepository;

import static ru.mshamanin.voteforlunch.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Menu save(Menu menu, int restaurantId) {
        menu.setRestaurant(restaurantRepository.getExisted(restaurantId));
        return menuRepository.save(menu);
    }
}
