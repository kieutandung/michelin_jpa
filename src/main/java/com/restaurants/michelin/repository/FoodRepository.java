package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.FoodStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    @Query("SELECT f FROM Food f " +
            "WHERE LOWER(f.nameFood) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.type) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY f.idFood DESC")
    List<Food> searchByKeyword(@Param("keyword") String keyword);

    Page<Food> findAllByStatusOrderByIdFoodDesc(FoodStatus status, Pageable pageable);
    List<Food> findAllFoodByStatusOrderByIdFoodDesc(FoodStatus status);
}
