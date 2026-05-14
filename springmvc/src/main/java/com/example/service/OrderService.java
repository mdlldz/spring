package com.example.service;

import com.example.entity.Order;
import java.util.List;

/**
 * 订单业务接口
 */
public interface OrderService {
    List<Order> findAll();
    Order findById(Integer id);
    void add(Order order);
    void update(Order order);
    void deleteById(Integer id);
}