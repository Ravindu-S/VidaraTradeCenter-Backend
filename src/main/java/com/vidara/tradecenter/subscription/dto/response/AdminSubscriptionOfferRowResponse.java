package com.vidara.tradecenter.subscription.dto.response;

import java.math.BigDecimal;

public class AdminSubscriptionOfferRowResponse {

    private Long productId;
    private String productName;
    private String sku;
    private boolean offerConfigured;
    private boolean subscriptionEnabled;
    private BigDecimal discountPercent;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isOfferConfigured() {
        return offerConfigured;
    }

    public void setOfferConfigured(boolean offerConfigured) {
        this.offerConfigured = offerConfigured;
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
}
