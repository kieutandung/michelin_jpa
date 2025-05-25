package com.restaurants.michelin.service;

import java.util.List;

public interface UserService<T> {
    T findByEmail(String email);
    List<T> findAll();
    void save(T t);
    T findById(Integer id);
    void delete(Integer id);
}
