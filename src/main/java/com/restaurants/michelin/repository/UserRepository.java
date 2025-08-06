package com.restaurants.michelin.repository;

import com.restaurants.michelin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByName(String name);
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY u.idUser DESC")
    List<User> searchByName(@Param("keyword") String keyword);


}