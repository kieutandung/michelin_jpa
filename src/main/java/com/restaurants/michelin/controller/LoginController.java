package com.restaurants.michelin.controller;

import com.restaurants.michelin.model.User;
import com.restaurants.michelin.model.UserStatus;
import com.restaurants.michelin.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/michelin")
public class LoginController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String viewLogin() {
        return "/login";
    }

    @PostMapping("/home")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {

        User user = userService.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            if (user.getStatus() != UserStatus.active) {
                model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
                return "/login";
            }

            model.addAttribute("user", user);
            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "/food/homeAdmin";
            } else {
                return "/homeUser";
            }
        } else {
            model.addAttribute("error", "Sai email hoặc mật khẩu.");
            return "/login";
        }
    }
}
