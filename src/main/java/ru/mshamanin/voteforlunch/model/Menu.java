package ru.mshamanin.voteforlunch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date", "name"},
        name = "menus_unique_restaurant_date_name_idx")})
public class Menu extends AbstractNamedEntity {

    @Column(name = "price", precision = 16, scale = 2, nullable = false)
    @Range(min = 1)
    @NotNull
    private BigDecimal price;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Restaurant restaurant;

    public Menu(Integer id, String name, BigDecimal price, LocalDate date, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.date = date;
        this.restaurant = restaurant;
    }

    public Menu(Menu menu) {
        super(menu.getId(), menu.getName());
        this.price = menu.getPrice();
        this.date = menu.getDate();
        this.restaurant = menu.getRestaurant();
    }


    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", name=" + name + ", price=" + price + ", date=" + date + "}";
    }
}
