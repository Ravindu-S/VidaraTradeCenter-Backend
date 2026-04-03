package com.vidara.tradecenter.order.dto;

import java.time.LocalDateTime;

public class OrderStatusResponse {

    private Long orderId;
    private String orderNumber;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdatedAt;

    public OrderStatusResponse() {
    }

    public OrderStatusResponse(Long orderId, String orderNumber, String orderStatus,
                               String paymentStatus, LocalDateTime orderDate, LocalDateTime lastUpdatedAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.orderDate = orderDate;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
