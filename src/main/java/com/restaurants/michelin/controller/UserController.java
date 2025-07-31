package com.restaurants.michelin.controller;
import com.restaurants.michelin.model.*;
import com.restaurants.michelin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/michelin/user/home")
public class UserController {
    @Autowired
    private FoodServiceImpl foodService;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemServiceImpl orderItemService;
    @Autowired
    private UserServiceImpl userService;
    @GetMapping("")
    public String home(Model model){
        model.addAttribute("foods", foodService.findAllFoodByStatusOrderByIdFoodDesc(FoodStatus.Còn_bán));
        return "/user/home/food";
    }
    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/michelin/login";
        }
        List<Cart> carts = cartService.findCartByUser(user);
        int totalPrice = carts.stream()
                .filter(cart -> cart.getFood() != null)
                .mapToInt(cart -> cart.getFood().getPrice() * cart.getQuantity())
                .sum();
        int serviceFee = totalPrice / 20;
        int vatFee = (int)(totalPrice * 0.084);
        int grandTotal = totalPrice + serviceFee + vatFee;

        model.addAttribute("cartItems", carts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("serviceFee", serviceFee);
        model.addAttribute("vatFee", vatFee);
        model.addAttribute("grandTotal", grandTotal);
        return "/user/home/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam("idFood") int idFood, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/michelin/login";
        }
        Food food = foodService.findById(idFood);
        if (food == null) {
            return "redirect:/michelin/user/home?error=foodNotFound";
        }
        Cart cart = cartService.findByUserAndFood(user, food);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + 1);
            cartService.save(cart);
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setFood(food);
            newCart.setQuantity(1);
            cartService.save(newCart);
        }
        return "redirect:/michelin/user/home/cart";
    }
    @PostMapping("/cart/remove/{idCart}")
    public String removeCartItem(@PathVariable("idCart") Integer idCart, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        cartService.removeItemFromCart(idCart, user);
        return "redirect:/michelin/user/home/cart";
    }
    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("action") String action,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/michelin/login";
        }

        String[] parts = action.split("-");
        if (parts.length == 2) {
            String command = parts[0];
            Integer idCart = Integer.parseInt(parts[1]);

            Cart cart = cartService.getCartByIdCart(idCart);
            if (cart != null && cart.getUser().getIdUser().equals(user.getIdUser())) {
                int currentQty = cart.getQuantity();

                if ("increase".equals(command)) {
                    cartService.updateQuantity(idCart, currentQty + 1);
                } else if ("decrease".equals(command)) {
                    if (currentQty > 1) {
                        cartService.updateQuantity(idCart, currentQty - 1);
                    }
                }
            }
        }

        return "redirect:/michelin/user/home/cart";
    }
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/michelin/login";
        }
        List<Cart> carts = cartService.findCartByUser(user);

        int totalPrice = carts.stream()
                .filter(cart -> cart.getFood() != null)
                .mapToInt(cart -> cart.getFood().getPrice() * cart.getQuantity())
                .sum();
        int serviceFee = totalPrice / 20;
        int vatFee = (int)(totalPrice * 0.084);
        int grandTotal = totalPrice + serviceFee + vatFee;

        model.addAttribute("user", user);
        model.addAttribute("cartItems", carts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("serviceFee", serviceFee);
        model.addAttribute("vatFee", vatFee);
        model.addAttribute("grandTotal", grandTotal);

        return "/user/home/checkout";
    }
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/michelin/login";

        orderService.placeOrder(user);
        return "redirect:/michelin/user/home/orders";
    }

    @GetMapping("/orders")
    public String viewMyOrders(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/michelin/login";
        }
        List<Order> orders = orderService.getOrdersByUser(loggedInUser.getIdUser());
        model.addAttribute("orders", orders);
        return "/user/order/list";
    }


    @GetMapping("/order/{id}")
    public String getOrderDetail(@PathVariable("id") Integer id, Model model,HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "/user/order/detail";
    }

    @GetMapping ("/search/food")
    public String searchFood(@RequestParam("keyword") String keyword, Model model) {
        List<Food> foods = foodService.searchByName(keyword);
        model.addAttribute("foods", foods);
        return "/user/home/food";
    }
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/michelin/login";
        }

        User user = userService.findById(loggedInUser.getIdUser());
        model.addAttribute("user", user);
        return "/user/account/profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                                @RequestParam("avatar") MultipartFile avatar,
                                HttpSession session,
                                HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("loggedInUser");
        if (sessionUser == null) return "redirect:/michelin/login";

        user.setIdUser(sessionUser.getIdUser());
        userService.updateProfile(user, avatar, request);

        User updatedUser = userService.findById(user.getIdUser());
        session.setAttribute("loggedInUser", updatedUser);

        return "redirect:/michelin/user/home/profile";
    }
    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestParam("orderId") Integer orderId) {
        Order order = orderService.findById(orderId);
        if (order != null && order.getStatus() != OrderStatus.Cancelled) {
            order.setStatus(OrderStatus.Cancelled);
            orderService.save(order);
        }
        return "redirect:/michelin/user/home/orders";
    }
    @PostMapping("/order/pay/{id}")
    public String payOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderService.findById(id));
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (order.getStatus() == OrderStatus.Delivering) {
                order.setStatus(OrderStatus.Completed);
                orderService.save(order);
                redirectAttributes.addFlashAttribute("message", "Thanh toán thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không thể thanh toán đơn hàng này.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng.");
        }
        return "redirect:/michelin/user/home/orders";
    }
    @GetMapping("/search/user")
    public String searchUser(@RequestParam("keyword") String keyword, Model model) {
        List<User> users = userService.searchByName(keyword);
        model.addAttribute("users", users);
        return "/admin/account/list";
    }
}
