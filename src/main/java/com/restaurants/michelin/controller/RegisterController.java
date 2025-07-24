package com.restaurants.michelin.controller;

import com.restaurants.michelin.model.User;
import com.restaurants.michelin.model.UserRole;
import com.restaurants.michelin.model.UserStatus;
import com.restaurants.michelin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/michelin")
public class RegisterController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "/authenticate/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email đã được sử dụng.");
            return "/authenticate/register";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp.");
            return "/authenticate/register";
        }

        user.setRole(UserRole.valueOf("user"));
        user.setStatus(UserStatus.valueOf("active"));

        userService.save(user);
        return "redirect:/michelin/login";
    }

}
