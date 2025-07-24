package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.Cart;
import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findCartByUser(User user);
    Cart findByUserAndFood(User user, Food food);
    Cart findByIdCartAndUser(Integer idCart, User user);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.idCart = :cartId")
    void updateQuantity(@Param("cartId") Integer cartId, @Param("quantity") int quantity);

    Cart getCartByIdCart(Integer idCart);

    @Query("SELECT c FROM Cart c WHERE c.user = :user")
    List<Cart> findByUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user = :user")
    void deleteByUser(@Param("user") User user);
}
