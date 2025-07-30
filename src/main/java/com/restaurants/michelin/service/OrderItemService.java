package com.restaurants.michelin.service;

import java.util.List;

public interface OrderItemService<O> {
    List<O> getOrderItemsByOrderId(Integer orderId);

}
