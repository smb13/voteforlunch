package ru.mshamanin.voteforlunch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menus", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "name", "restaurant_id"},
                name = "menus_unique_date_name_restaurant_id_idx")})
public class Menu extends AbstractNamedEntity {

    @Column(name = "price", precision = 16, scale = 2, nullable = false)
    @Range(min = 1)
    @NotNull
    private BigDecimal price;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Restaurant restaurant;

    public Menu() {
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name=" + name +
                ", price=" + price +
                ", date=" + date +
                ", restaurant=" + restaurant +
                "}";
    }
}
