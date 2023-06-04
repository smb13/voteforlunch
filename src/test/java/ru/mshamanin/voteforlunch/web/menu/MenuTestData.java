package ru.mshamanin.voteforlunch.web.menu;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Menu;
import ru.mshamanin.voteforlunch.model.Restaurant;
import ru.mshamanin.voteforlunch.model.Vote;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.RoundingMode.CEILING;
import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant1;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant2;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.anotherUser;
import static ru.mshamanin.voteforlunch.web.user.UserTestData.user;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant");

    public static final int RESTAURANT1_MENU1_ID = START_SEQ + 6;
    public static final int RESTAURANT2_MENU1_ID = START_SEQ + 12;
    public static final int MENU_ID_NOT_FOUND = START_SEQ + 30;

    public static final Menu rest1Menu1 = new Menu(RESTAURANT1_MENU1_ID, "Бефстроганов", BigDecimal.valueOf(350.00).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 29), restaurant1);
    public static final Menu rest1Menu2 = new Menu(RESTAURANT1_MENU1_ID + 1, "Сырная тарелка", BigDecimal.valueOf(231.00).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 29), restaurant1);
    public static final Menu rest1Menu3 = new Menu(RESTAURANT1_MENU1_ID + 2, "Котлета по-киевски", BigDecimal.valueOf(205.00).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 30), restaurant1);
    public static final Menu rest1Menu4 = new Menu(RESTAURANT1_MENU1_ID + 3, "Салат греческий", BigDecimal.valueOf(310.00).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 30), restaurant1);
    public static final Menu rest1Menu5 = new Menu(RESTAURANT1_MENU1_ID + 4, "Котлета по-киевски", BigDecimal.valueOf(208.00).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 31), restaurant1);
    public static final Menu rest1Menu6 = new Menu(RESTAURANT1_MENU1_ID + 5, "Салат греческий", BigDecimal.valueOf(313.50).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 31), restaurant1);

    public static final List<Menu> rest1MenusFor20230130 = List.of(rest1Menu3, rest1Menu4);
    public static final List<Menu> rest1Menus = List.of(rest1Menu1, rest1Menu2, rest1Menu3, rest1Menu4, rest1Menu5, rest1Menu6);

    public static Menu getNew() {
        return new Menu(null, "Борщ украинский", BigDecimal.valueOf(241.20).setScale(2, CEILING), LocalDate.of(2023, Month.JANUARY, 30), null);
    }

    public static Menu getUpdated(){
        Menu updated = new Menu(rest1Menu1);
        updated.setPrice(BigDecimal.valueOf(351.00).setScale(2, CEILING));
        return updated;
    }
}
