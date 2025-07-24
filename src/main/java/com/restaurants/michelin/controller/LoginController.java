package com.restaurants.michelin.controller;

import com.restaurants.michelin.model.User;
import com.restaurants.michelin.model.UserRole;
import com.restaurants.michelin.model.UserStatus;
import com.restaurants.michelin.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/michelin")
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/login")
    public String viewLogin() {
        return "/authenticate/login";
    }

    @PostMapping("/home")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession session) {

        User user = userService.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai email hoặc mật khẩu.");
            return "/authenticate/login";
        }

        if (user.getStatus() != UserStatus.active) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt.");
            return "/authenticate/login";
        }

        session.setAttribute("loggedInUser", user);

        return user.getRole() == UserRole.admin
                ? "redirect:/michelin/home"
                : "redirect:/michelin/user/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/michelin/login";
    }
}
