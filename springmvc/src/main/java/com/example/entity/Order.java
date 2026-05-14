package com.example.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类（扩展业务复杂度）
 * 功能：模拟订单数据模型，用于REST接口、模型数据传递
 */
public class Order {
    private Integer id;          // 订单ID
    private String orderNo;     // 订单编号
    private BigDecimal amount;  // 订单金额
    private Date createTime;    // 创建时间
    private Integer userId;     // 所属用户ID
    private String status;      // 订单状态

    // 无参构造
    public Order() {}

    // 全参构造
    public Order(Integer id, String orderNo, BigDecimal amount, Date createTime, Integer userId, String status) {
        this.id = id;
        this.orderNo = orderNo;
        this.amount = amount;
        this.createTime = createTime;
        this.userId = userId;
        this.status = status;
    }

    // Getter/Setter
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", amount=" + amount +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}