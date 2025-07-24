package com.restaurants.michelin.model;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCart;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "idFood", nullable = false)
    private Food food;

    private int quantity;

    public Cart() {}

    public Cart(Integer idCart, User user, Food food, int quantity) {
        this.idCart = idCart;
        this.user = user;
        this.food = food;
        this.quantity = quantity;
    }


    public Integer getIdCart() {
        return idCart;
    }

    public void setIdCart(Integer idCart) {
        this.idCart = idCart;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
