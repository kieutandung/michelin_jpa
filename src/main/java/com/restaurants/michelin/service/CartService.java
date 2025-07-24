package com.restaurants.michelin.service;


import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.User;
import java.util.List;


public interface CartService<T> {
    List<T> findCartByUser(User user);
    void save(T t);
    T findByUserAndFood(User user, Food food);
    void removeItemFromCart(Integer idCart, User user);
    void updateQuantity(Integer idCart, int newQuantity);
    T getCartByIdCart(Integer idCart);
    void deleteAllCartByUser(User user);
}
