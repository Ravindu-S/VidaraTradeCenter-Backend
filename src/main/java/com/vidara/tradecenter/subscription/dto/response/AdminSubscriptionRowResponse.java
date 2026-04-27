package com.vidara.tradecenter.subscription.dto.response;

import com.vidara.tradecenter.subscription.model.enums.DeliveryFrequency;
import com.vidara.tradecenter.subscription.model.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminSubscriptionRowResponse {

    private Long id;
    private Long userId;
    private String customerEmail;
    private Long productId;
    private String productName;
    private DeliveryFrequency frequency;
    private int quantity;
    private SubscriptionStatus status;
    private BigDecimal unitPriceSnapshot;
    private BigDecimal discountPercentApplied;
    private LocalDate nextBillingDate;
    private LocalDateTime cancelledAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public DeliveryFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(DeliveryFrequency frequency) {
        this.frequency = frequency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public BigDecimal getUnitPriceSnapshot() {
        return unitPriceSnapshot;
    }

    public void setUnitPriceSnapshot(BigDecimal unitPriceSnapshot) {
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public BigDecimal getDiscountPercentApplied() {
        return discountPercentApplied;
    }

    public void setDiscountPercentApplied(BigDecimal discountPercentApplied) {
        this.discountPercentApplied = discountPercentApplied;
    }

    public LocalDate getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDate nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
