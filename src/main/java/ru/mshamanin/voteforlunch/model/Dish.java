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

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "name"},
        name = "dishes_unique_menu_name_idx")})
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", precision = 16, scale = 2, nullable = false)
    @Range(min = 1)
    @NotNull
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Menu menu;

    public Dish(Integer id, String name, BigDecimal price, Menu menu) {
        super(id, name);
        this.price = price;
        this.menu = menu;
    }

    public Dish(Dish dish) {
        super(dish.getId(), dish.getName());
        this.price = dish.getPrice();
        this.menu = dish.getMenu();
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", price=" + price +
                "}";
    }
}
