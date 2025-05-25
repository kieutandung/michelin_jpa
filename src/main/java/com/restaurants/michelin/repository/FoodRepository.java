package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {
}
