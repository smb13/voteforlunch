package ru.mshamanin.voteforlunch.web.dish;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Dish;
import ru.mshamanin.voteforlunch.model.Menu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.math.RoundingMode.CEILING;
import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;
import static ru.mshamanin.voteforlunch.web.menu.MenuTestData.*;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant1;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "menu");

    public static final int RESTAURANT1_MENU1_DISH1_ID = START_SEQ + 12;
    public static final int RESTAURANT1_MENU2_DISH1_ID = START_SEQ + 14;

    public static final Dish rest1Menu1Dish1 = new Dish(RESTAURANT1_MENU1_DISH1_ID, "Бефстроганов", BigDecimal.valueOf(350.00).setScale(2, CEILING), rest1Menu1);
    public static final Dish rest1Menu1Dish2 = new Dish(RESTAURANT1_MENU1_DISH1_ID + 1, "Сырная тарелка", BigDecimal.valueOf(231.00).setScale(2, CEILING), rest1Menu1);

    public static final List<Dish> rest1Menu1Dishes = List.of(rest1Menu1Dish1, rest1Menu1Dish2);

    public static Dish getNew() {
        return new Dish(null, "Борщ украинский", BigDecimal.valueOf(241.20).setScale(2, CEILING), null);
    }

    public static Dish getUpdated(){
        Dish updated = new Dish(rest1Menu1Dish1);
        updated.setPrice(BigDecimal.valueOf(351.00).setScale(2, CEILING));
        return updated;
    }
}
