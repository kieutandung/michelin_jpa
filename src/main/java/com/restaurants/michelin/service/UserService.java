package com.restaurants.michelin.service;

import com.restaurants.michelin.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService<T> {
    T findByEmail(String email);
    List<T> findAll();
    void save(T t);
    T findById(Integer id);
    void delete(Integer id);
    T findByName(String name);
    void updateProfile(User user, MultipartFile avatar, HttpServletRequest request);
    List<T> searchByName(String keyword);

}
