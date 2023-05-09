package ru.mshamanin.voteforlunch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "restaurants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"},
                name = "restaurants_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant restaurant) {
        super(restaurant.getId(), restaurant.getName());
    }
}
