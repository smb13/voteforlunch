package ru.mshamanin.voteforlunch.web.restaurant;

import ru.mshamanin.voteforlunch.MatcherFactory;
import ru.mshamanin.voteforlunch.model.Restaurant;

import static ru.mshamanin.voteforlunch.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = START_SEQ + 3;
    public static final int RESTAURANT2_ID = START_SEQ + 4;
    public static final int RESTAURANT3_ID = START_SEQ + 5;
    public static final int RESTAURANT_ID_NOT_FOUND = START_SEQ + 10;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Ресторан Бабель");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Ткемали");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Маленькая Италия");
    public static final String pieceOfRestaurantsNames = "али";
    public static Restaurant getUpdated(){
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName("Ресторан Гольштейн");
        return updated;
    }

    public static Restaurant getNew() {
        Restaurant updated = new Restaurant(null, "Новый ресторан");
        return updated;
    }
}
