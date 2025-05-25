package com.restaurants.michelin.service;

import java.util.List;

public interface FoodService<T> {
    List<T> findAll();
    void save(T t);
    T findById(Integer id);
    void delete(Integer id);
}
