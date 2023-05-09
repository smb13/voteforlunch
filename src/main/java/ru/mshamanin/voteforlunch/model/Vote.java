package ru.mshamanin.voteforlunch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "restaurant_id", "date"},
                name = "votes_unique_user_restaurant_date_idx")})
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    @NotNull
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    public Vote() {
    }

    public Vote(Integer id, LocalDate date, Restaurant restaurant, User user) {
        super(id);
        this.date = date;
        this.restaurant = restaurant;
        this.user = user;
    }

    public Vote(Vote vote) {
        super(vote.getId());
        this.date = vote.getDate();
        this.restaurant = vote.getRestaurant();
        this.user = vote.getUser();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + date +
                ", user=" + user +
                ", date=" + restaurant +
                ", restaurant=" + user +
                '}';
    }
}
