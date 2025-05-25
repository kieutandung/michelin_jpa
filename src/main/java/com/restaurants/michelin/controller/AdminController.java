package com.restaurants.michelin.controller;

import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.User;
import com.restaurants.michelin.service.FoodServiceImpl;
import com.restaurants.michelin.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/michelin/home")
public class AdminController {
    @Autowired private FoodServiceImpl foodService;
    @Autowired private UserServiceImpl userService;
    @GetMapping("")
    public String home(Model model){
        model.addAttribute("foods", foodService.findAll());
        return "/food/homeAdmin";
    }
    @GetMapping("/add")
    public String viewAddFood(Model model){
        model.addAttribute("food" , new Food());
        return "/food/add";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("food") Food food){
        foodService.save(food);
        return "redirect:/michelin/home";
    }
    @GetMapping("/{id}/edit")
    public String viewEditFood(@PathVariable Integer id , Model model){
        model.addAttribute("food" , foodService.findById(id));
        return "/food/edit";
    }
    @PostMapping("/{id}")
    public String delete(@PathVariable Integer id){
        foodService.delete(id);
        return "redirect:/michelin/home";
    }

    //user
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/user/list";
    }

    @GetMapping("/users/add")
    public String viewAddUser(Model model) {
        model.addAttribute("user", new User());
        return "/user/add";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/michelin/home/users";
    }

    @GetMapping("/users/{id}/edit")
    public String viewEditUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "/user/edit";
    }

    @PostMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return "redirect:/michelin/home/users";
    }
}
