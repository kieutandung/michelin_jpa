package com.restaurants.michelin.repository;


import com.restaurants.michelin.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_IdOrder(Integer idOrder);
    @Query("SELECT oi.food AS food, SUM(oi.quantity) AS totalSold " +
            "FROM OrderItem oi " +
            "WHERE oi.order.status = 'Completed' AND oi.food.status = 'Còn_bán' " +
            "GROUP BY oi.food " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopBestSellingFoods(Pageable pageable);

}

