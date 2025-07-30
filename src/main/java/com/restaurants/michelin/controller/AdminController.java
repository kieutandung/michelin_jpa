package com.restaurants.michelin.controller;

import com.restaurants.michelin.model.Food;
import com.restaurants.michelin.model.FoodStatus;
import com.restaurants.michelin.model.Order;
import com.restaurants.michelin.model.User;
import com.restaurants.michelin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/michelin/home")
public class AdminController {
    @Autowired private FoodServiceImpl foodService;
    @Autowired private UserServiceImpl userService;
    @Autowired private OrderService orderService;
    @Autowired private OrderItemServiceImpl orderItemService;
    @GetMapping("")
    public String home(Model model){
        model.addAttribute("foods", foodService.findAllByStatusOrderByIdFoodDesc(FoodStatus.Còn_bán));
        return "/admin/food/homeAdmin";
    }
    @GetMapping("/add")
    public String viewAddFood(Model model){
        model.addAttribute("food" , new Food());
        return "/admin/food/add";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("food") Food food,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       @RequestParam(value = "oldImage", required = false) String oldImage) {

        if (!imageFile.isEmpty()) {
            food.setImage(imageFile.getOriginalFilename());
        } else {
            food.setImage(oldImage);
        }

        foodService.save(food);
        return "redirect:/michelin/home";
    }

    @GetMapping("/{id}/edit")
    public String viewEditFood(@PathVariable Integer id , Model model){
        model.addAttribute("food" , foodService.findById(id));
        return "/admin/food/edit";
    }
    @PostMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        foodService.markAsSoldOut(id); // đổi tên cho rõ nghĩa
        return "redirect:/michelin/home";
    }


    //user
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/account/list";
    }

    @GetMapping("/users/add")
    public String viewAddUser(Model model) {
        model.addAttribute("user", new User());
        return "/admin/account/add";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "/admin/account/add";
        }

        userService.save(user);

        return "redirect:/michelin/home/users";
    }


    @GetMapping("/users/{id}/edit")
    public String viewEditUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "/admin/account/edit";
    }

    @PostMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return "redirect:/michelin/home/users";
    }
    @GetMapping ("/search")
    public String searchFood(@RequestParam("keyword") String keyword, Model model) {
        List<Food> foods = foodService.searchByName(keyword);
        model.addAttribute("foods", foods);
        return "/admin/food/homeAdmin";
    }
    @GetMapping("/order")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.findAllByOrderByIdOrderDesc();
        model.addAttribute("orders", orders);
        return "/admin/revenue/order";
    }
    @GetMapping("/revenue")
    public String showRevenueChart(Model model) {
        List<Object[]> results = orderService.getMonthlyRevenue();

        List<String> months = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();

        for (Object[] row : results) {
            String monthStr = (String) row[0];
            Double revenue = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            String[] parts = monthStr.split("-");
            String formattedMonth = "Tháng " + Integer.parseInt(parts[1]) + "/" + parts[0];
            months.add(formattedMonth);
            revenues.add(revenue);
        }

        model.addAttribute("months", months);
        model.addAttribute("revenues", revenues);
        return "/admin/revenue/chart";
    }



}
