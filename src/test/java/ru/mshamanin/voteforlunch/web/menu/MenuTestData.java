package ru.mshamanin.voteforlunch.web.menu;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Menu;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;
import static ru.mshamanin.voteforlunch.web.restaurant.RestaurantTestData.restaurant1;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishes");
    public static MatcherFactory.Matcher<Menu> MENU_WITH_DISHES_MATCHER =
            MatcherFactory.usingAssertions(Menu.class,
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("restaurant", "dishes.menu").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int RESTAURANT1_MENU1_ID = START_SEQ + 6;
    public static final int RESTAURANT1_MENU2_ID = RESTAURANT1_MENU1_ID + 1;
    public static final int RESTAURANT2_MENU1_ID = START_SEQ + 9;
    public static final int MENU_ID_NOT_FOUND = START_SEQ + 30;

    public static final Menu rest1Menu1 = new Menu(RESTAURANT1_MENU1_ID, LocalDate.of(2023, Month.JANUARY, 29), restaurant1);
    public static final Menu rest1Menu2 = new Menu(RESTAURANT1_MENU1_ID + 1, LocalDate.of(2023, Month.JANUARY, 30), restaurant1);
    public static final Menu rest1Menu3 = new Menu(RESTAURANT1_MENU1_ID + 2, LocalDate.of(2023, Month.JANUARY, 31), restaurant1);
    public static final Menu rest2Menu1 = new Menu(RESTAURANT1_MENU1_ID + 3, LocalDate.of(2023, Month.JANUARY, 30), restaurant1);
    public static final List<Menu> rest1MenusFor20230130 = List.of(rest1Menu2);
    public static final List<Menu> rest1Menus = List.of(rest1Menu1, rest1Menu2, rest1Menu3);

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2023, Month.JANUARY, 30), null);
    }

    public static Menu getUpdated() {
        Menu updated = new Menu(rest1Menu1);
        updated.setDate(LocalDate.of(2023, Month.JANUARY, 31));
        return updated;
    }
}
