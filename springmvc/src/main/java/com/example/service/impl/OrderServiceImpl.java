package com.example.service.impl;

import com.example.dao.OrderDao;
import com.example.entity.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单业务实现类
 * 功能：处理订单业务逻辑，适配REST接口与模型数据传递
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public Order findById(Integer id) {
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    @Override
    public void add(Order order) {
        if (order.getAmount() == null || order.getAmount().compareTo(new BigDecimal("0")) <= 0) {
            throw new RuntimeException("订单金额不合法");
        }
        orderDao.add(order);
    }

    @Override
    public void update(Order order) {
        if (order.getId() == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        orderDao.update(order);
    }

    @Override
    public void deleteById(Integer id) {
        orderDao.deleteById(id);
    }
}