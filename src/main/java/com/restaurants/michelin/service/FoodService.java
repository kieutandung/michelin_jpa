package com.restaurants.michelin.service;

import com.restaurants.michelin.model.FoodStatus;

import java.util.List;

public interface FoodService<T> {
    List<T> findAll();
    void save(T t);
    T findById(Integer id);
    void delete(Integer id);
    List<T> searchByName(String keyword);
    List<T> findAllByStatusOrderByIdFoodDesc(FoodStatus status);
    void markAsSoldOut(Integer id);

}
