package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user.idUser = :userId ORDER BY o.idOrder DESC")
    List<Order> findAllByUserWithItems(@Param("userId") Integer userId);
    List<Order> findAllByOrderByIdOrderDesc();

    @Query("SELECT FUNCTION('DATE_FORMAT', o.orderDate, '%Y-%m') AS month, SUM(o.totalPrice) AS revenue " +
            "FROM Order o WHERE o.status = 'Completed' " +
            "GROUP BY FUNCTION('DATE_FORMAT', o.orderDate, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', o.orderDate, '%Y-%m')")
    List<Object[]> getMonthlyRevenue();
}
