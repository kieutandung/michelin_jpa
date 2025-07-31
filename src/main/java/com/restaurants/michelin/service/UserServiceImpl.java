package com.restaurants.michelin.service;

import com.restaurants.michelin.model.User;
import com.restaurants.michelin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService<User>{
    @Autowired
    UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(findById(id));
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
    @Override
    public void updateProfile(User user, MultipartFile avatar, HttpServletRequest request) {
        User existing = userRepository.findById(user.getIdUser()).orElse(null);
        if (existing == null) return;

        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setAddress(user.getAddress());

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
                String uploadPath = request.getServletContext().getRealPath("/image/");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                File savedFile = new File(uploadPath + fileName);
                avatar.transferTo(savedFile);
                existing.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userRepository.save(existing);
    }

    @Override
    public List<User> searchByName(String keyword) {
        return userRepository.searchByName(keyword);
    }
}
