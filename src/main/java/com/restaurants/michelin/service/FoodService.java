package com.restaurants.michelin.service;

import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.FoodStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService<T> {
    List<T> findAll();
    void save(T t);
    T findById(Integer id);
    void delete(Integer id);
    List<T> searchByName(String keyword);
    Page<T> findAllByStatusOrderByIdFoodDesc(FoodStatus status, Pageable pageable);
    void markAsSoldOut(Integer id);
    Page<T> findByStatusAndDiscountOrderByIdFoodDesc(FoodStatus status, int discount, Pageable pageable);    List<T> getDiscountedFoods();
    List<T> findAllDiscountedFoods();
    Page<Food> searchFoodByKeywordAndStatusAndDiscount(String keyword, FoodStatus status, int discount, Pageable pageable);
}
