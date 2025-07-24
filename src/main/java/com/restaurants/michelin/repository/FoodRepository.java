package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    @Query("SELECT f FROM Food f WHERE LOWER(f.nameFood) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Food> searchByKeyword(@Param("keyword") String keyword);


}
