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
import java.util.ArrayList;
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
    public String home(Model model, HttpSession session) {
        model.addAttribute("foods", foodService.getTop5BestSellingFoods());
        model.addAttribute("discountedFoods", foodService.getDiscountedFoods());

        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("cartCount", user != null ? cartService.countItemsInCart(user) : 0);

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
                .mapToInt(cart -> {
                    int price = cart.getFood().getPrice();
                    int discount = cart.getFood().getDiscount();
                    int finalPrice = (discount > 0) ? price * (100 - discount) / 100 : price;
                    return finalPrice * cart.getQuantity();
                })
                .sum();

        int serviceFee = totalPrice / 20; // 5%
        int vatFee = (int)(totalPrice * 0.084); // 8.4%
        int grandTotal = totalPrice + serviceFee + vatFee;

        model.addAttribute("cartItems", carts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("serviceFee", serviceFee);
        model.addAttribute("vatFee", vatFee);
        model.addAttribute("grandTotal", grandTotal);

        return "/user/home/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("idFood") int idFood, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/michelin/login";
        }

        Food food = foodService.findById(idFood);
        if (food == null) {
            redirectAttributes.addFlashAttribute("error", "Món ăn không tồn tại.");
            return "redirect:/michelin/user/home";
        }

        if (food.getQuantity() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Món ăn đã hết hàng!");
            return "redirect:/michelin/user/home";
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

        // ✅ Giảm số lượng tồn kho
        food.setQuantity(food.getQuantity() - 1);
        foodService.save(food);

        redirectAttributes.addFlashAttribute("message", "Đã thêm vào giỏ hàng!");
        return "redirect:/michelin/user/home";
    }


    @PostMapping("/cart/remove/{idCart}")
    public String removeCartItem(@PathVariable("idCart") Integer idCart, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/michelin/login";
        }

        Cart cart = cartService.getCartByIdCart(idCart);
        if (cart != null && cart.getUser().getIdUser().equals(user.getIdUser())) {
            int qtyToReturn = cart.getQuantity();
            Food food = cart.getFood();

            // ✅ Trả lại hàng tồn kho
            if (food != null) {
                food.setQuantity(food.getQuantity() + qtyToReturn);
                foodService.save(food);
            }

            cartService.removeItemFromCart(idCart, user);
        }

        return "redirect:/michelin/user/home/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("action") String action,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
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
                Food food = cart.getFood();

                if (food != null) {
                    if ("increase".equals(command)) {
                        if (food.getQuantity() > 0) {
                            cartService.updateQuantity(idCart, currentQty + 1);
                            food.setQuantity(food.getQuantity() - 1); // ✅ Trừ kho
                            foodService.save(food);
                        } else {
                            // ✅ Thêm thông báo hết hàng
                            redirectAttributes.addFlashAttribute("error", "Sản phẩm đã hết hàng !");
                        }
                    } else if ("decrease".equals(command)) {
                        if (currentQty > 1) {
                            cartService.updateQuantity(idCart, currentQty - 1);
                            food.setQuantity(food.getQuantity() + 1); // ✅ Trả kho
                            foodService.save(food);
                        }
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
                .mapToInt(cart -> {
                    int price = cart.getFood().getPrice();
                    int discount = cart.getFood().getDiscount();
                    int finalPrice = (discount > 0) ? price * (100 - discount) / 100 : price;
                    return finalPrice * cart.getQuantity();
                })
                .sum();

        int serviceFee = totalPrice / 20; // 5%
        int vatFee = (int)(totalPrice * 0.084); // 8.4%
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
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/michelin/login";

        // Gọi xử lý đặt hàng
        orderService.placeOrder(user);

        // Gửi thông báo về Home sau khi redirect
        redirectAttributes.addFlashAttribute("orderSuccess", "Đặt hàng thành công!");

        // Chuyển về trang chủ
        return "redirect:/michelin/user/home";
    }
    @GetMapping("/orders")
    public String viewMyOrders(@RequestParam(value = "status", required = false) String status,
                               HttpSession session,
                               Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/michelin/login";
        }

        List<Order> orders;

        if (status == null || status.isEmpty()) {
            // Không có trạng thái -> lấy tất cả
            orders = orderService.getOrdersByUser(loggedInUser.getIdUser());
        } else {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase()); // chuyển về chữ IN HOA
                orders = orderService.getOrdersByUserAndStatus(loggedInUser.getIdUser(), orderStatus);
            } catch (IllegalArgumentException e) {
                orders = new ArrayList<>();
            }
        }

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
    @GetMapping("/menu")
    public String menu(@RequestParam(value = "keyword", required = false) String keyword,
                       Model model,
                       HttpSession session) {
        List<Food> foods;

        if (keyword != null && !keyword.trim().isEmpty()) {
            foods = foodService.searchByName(keyword);
            model.addAttribute("keyword", keyword); // ✅ Gửi lại keyword để hiển thị
        } else {
            foods = foodService.findAllFoodByStatusOrderByIdFoodDesc(FoodStatus.Còn_bán);
        }

        model.addAttribute("menu", foods);

        // Lấy user từ session
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            int cartCount = cartService.countItemsInCart(user);
            model.addAttribute("cartCount", cartCount);
        } else {
            model.addAttribute("cartCount", 0);
        }

        return "/user/home/menu";
    }
    @GetMapping("/discounted")
    public String discounted(Model model) {
        List<Food> discountedFoods = foodService.findAllDiscountedFoods(); // Đã lọc còn bán
        model.addAttribute("discountedFoods", discountedFoods);
        return "/user/home/discounted";
    }


}
