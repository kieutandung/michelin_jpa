package com.restaurants.michelin.service;

import com.restaurants.michelin.model.*;
import com.restaurants.michelin.repository.CartRepository;
import com.restaurants.michelin.repository.OrderItemRepository;
import com.restaurants.michelin.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;
    @Transactional
    @Override
    public void placeOrder(User user) {
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) return;

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getFood().getPrice() * item.getQuantity())
                .sum();

        double serviceFee = totalPrice * 0.05;
        double vatFee = totalPrice * 0.1;
        double grandTotal = totalPrice + serviceFee + vatFee;

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(user.getName());
        order.setEmail(user.getEmail());
        order.setPhone(user.getPhone());
        order.setAddress(user.getAddress());
        order.setTotalPrice(totalPrice);
        order.setServiceFee(serviceFee);
        order.setVatFee(vatFee);
        order.setGrandTotal(grandTotal);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : cartItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setFood(cart.getFood());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getFood().getPrice());
            orderItems.add(item);
        }
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.Delivering);
        orderRepository.save(order);
        cartRepository.deleteByUser(user);
    }

    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        return orderRepository.findAllByUserWithItems(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> findAllByOrderByIdOrderDesc() {
        return orderRepository.findAllByOrderByIdOrderDesc();
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus() == OrderStatus.Delivering) {
                order.setStatus(OrderStatus.Cancelled);
                orderRepository.save(order);
            }
        }
    }

    @Override
    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
    }
    @Override
    public List<Object[]> getMonthlyRevenue() {
        return orderRepository.getMonthlyRevenue();
    }

}
