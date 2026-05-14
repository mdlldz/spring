package com.example.controller;

import com.example.entity.Order;
import com.example.entity.Result;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST风格订单控制器
 * 覆盖知识点：GET/POST/PUT/DELETE请求、@RequestBody、路径变量、RESTful设计
 */
@RestController
@RequestMapping("/api/orders")
public class RestOrderController {

    /**
     * 订单业务对象，由Spring自动注入
     */
    @Autowired
    private OrderService orderService;

    /**
     * 查询所有订单（GET请求）
     * @return 订单列表
     */
    @GetMapping
    public Result<List<Order>> listOrders() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }

    /**
     * 根据ID查询订单（GET请求，路径变量）
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.findById(id);
        return Result.success(order);
    }

    /**
     * 新增订单（POST请求，JSON请求体）
     * @param order 订单对象
     * @return 新增订单
     */
    @PostMapping
    public Result<Order> addOrder(@RequestBody Order order) {
        orderService.add(order);
        return Result.success(order);
    }

    /**
     * 更新订单（PUT请求，路径变量+JSON请求体）
     * @param id 订单ID
     * @param order 订单对象
     * @return 更新后的订单
     */
    @PutMapping("/{id}")
    public Result<Order> updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        order.setId(id);
        orderService.update(order);
        return Result.success(order);
    }

    /**
     * 删除订单（DELETE请求，路径变量）
     * @param id 订单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable("id") Integer id) {
        orderService.deleteById(id);
        return Result.success();
    }
}