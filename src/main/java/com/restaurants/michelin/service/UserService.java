package com.restaurants.michelin.service;

import com.restaurants.michelin.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    void save(User user);
    User findById(Integer id);
    void delete(Integer id);
    User findByName(String name);
    void updateProfile(User user, MultipartFile avatar, HttpServletRequest request);
    List<User> searchByName(String keyword);

}
