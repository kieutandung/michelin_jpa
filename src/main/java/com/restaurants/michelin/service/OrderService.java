package com.restaurants.michelin.service;

import com.restaurants.michelin.model.Order;
import com.restaurants.michelin.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void placeOrder(User user);
    List<Order> getOrdersByUser(Integer userId);
    Order getOrderById(Integer orderId);
    List<Order> getAllOrders();
    Order findById(Integer orderId);
    Page<Order> findAllByOrderByIdOrderDesc(Pageable pageable);
    void save(Order order);
    void cancelOrder(Integer orderId);
    List<Object[]> getMonthlyRevenue();

}