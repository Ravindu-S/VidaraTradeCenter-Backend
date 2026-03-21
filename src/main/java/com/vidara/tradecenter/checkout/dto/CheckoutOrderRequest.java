package com.vidara.tradecenter.checkout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CheckoutOrderRequest {

    private String purchaseType;
    private String subscriptionInterval;

    @NotNull
    private Long shippingAddressId;

    @NotEmpty
    @Valid
    private List<CheckoutItemRequest> items;

    public CheckoutOrderRequest() {
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getSubscriptionInterval() {
        return subscriptionInterval;
    }

    public void setSubscriptionInterval(String subscriptionInterval) {
        this.subscriptionInterval = subscriptionInterval;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public List<CheckoutItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItemRequest> items) {
        this.items = items;
    }
}

