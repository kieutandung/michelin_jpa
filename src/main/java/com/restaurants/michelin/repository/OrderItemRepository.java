package com.restaurants.michelin.repository;


import com.restaurants.michelin.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_IdOrder(Integer idOrder);}
