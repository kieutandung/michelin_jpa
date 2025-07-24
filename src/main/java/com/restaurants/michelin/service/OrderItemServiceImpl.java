package com.restaurants.michelin.service;

import com.restaurants.michelin.model.OrderItem;
import com.restaurants.michelin.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService<OrderItem> {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrder_IdOrder(orderId);
    }
}
