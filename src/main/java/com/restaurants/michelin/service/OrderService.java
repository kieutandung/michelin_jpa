package com.restaurants.michelin.service;

import com.restaurants.michelin.model.Order;
import com.restaurants.michelin.model.User;

import java.util.List;

public interface OrderService {
    void placeOrder(User user);
    List<Order> getOrdersByUser(Integer userId);
    Order getOrderById(Integer orderId);
    List<Order> getAllOrders();
    Order findById(Integer orderId);
    List<Order> findAllByOrderByIdOrderDesc();
    void save(Order order);
    void cancelOrder(Integer orderId);
}