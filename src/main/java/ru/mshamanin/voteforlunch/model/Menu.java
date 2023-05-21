package ru.mshamanin.voteforlunch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menus", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "date"},
        name = "menus_unique_restaurant_date_idx")})
public class Menu extends AbstractBaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(hidden = true)
    private List<Dish> dishes;

    public Menu(Integer id, LocalDate date, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
    }

    public Menu(Menu menu) {
        super(menu.getId());
        this.date = menu.getDate();
        this.restaurant = menu.getRestaurant();
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", date=" + date + "}";
    }
}
