package com.example.dao;

import com.example.entity.Order;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单数据访问层
 * 功能：模拟订单CRUD操作，适配REST接口与模型数据传递
 */
@Repository
public class OrderDao {
    private static final List<Order> orderList = new ArrayList<>();

    // 初始化模拟订单数据
    static {
        Order o1 = new Order(1, UUID.randomUUID().toString().substring(0, 8), new BigDecimal("299.00"), new Date(), 1, "已支付");
        Order o2 = new Order(2, UUID.randomUUID().toString().substring(0, 8), new BigDecimal("599.00"), new Date(), 1, "待发货");
        orderList.add(o1);
        orderList.add(o2);
    }

    /**
     * 查询所有订单
     * @return 订单列表
     */
    public List<Order> findAll() {
        return new ArrayList<>(orderList);
    }

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单对象
     */
    public Order findById(Integer id) {
        for (Order order : orderList) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    /**
     * 新增订单
     * @param order 订单对象
     */
    public void add(Order order) {
        order.setId(orderList.size() + 1);
        order.setCreateTime(new Date());
        order.setOrderNo(UUID.randomUUID().toString().substring(0, 8));
        orderList.add(order);
    }

    /**
     * 更新订单状态
     * @param order 订单对象
     */
    public void update(Order order) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getId().equals(order.getId())) {
                orderList.set(i, order);
                break;
            }
        }
    }

    /**
     * 删除订单
     * @param id 订单ID
     */
    public void deleteById(Integer id) {
        orderList.removeIf(o -> o.getId().equals(id));
    }
}