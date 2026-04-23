package com.vidara.tradecenter.subscription.dto.response;

import java.math.BigDecimal;

public class SubscriptionOfferPublicResponse {

    private Long productId;
    private boolean subscriptionEnabled;
    private BigDecimal discountPercent;
    private BigDecimal retailUnitPrice;
    private BigDecimal subscriptionUnitPrice;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public boolean isSubscriptionEnabled() {
        return subscriptionEnabled;
    }

    public void setSubscriptionEnabled(boolean subscriptionEnabled) {
        this.subscriptionEnabled = subscriptionEnabled;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getRetailUnitPrice() {
        return retailUnitPrice;
    }

    public void setRetailUnitPrice(BigDecimal retailUnitPrice) {
        this.retailUnitPrice = retailUnitPrice;
    }

    public BigDecimal getSubscriptionUnitPrice() {
        return subscriptionUnitPrice;
    }

    public void setSubscriptionUnitPrice(BigDecimal subscriptionUnitPrice) {
        this.subscriptionUnitPrice = subscriptionUnitPrice;
    }
}
