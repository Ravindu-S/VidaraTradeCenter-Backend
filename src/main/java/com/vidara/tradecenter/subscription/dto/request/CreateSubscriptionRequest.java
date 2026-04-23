package com.vidara.tradecenter.subscription.dto.request;

import com.vidara.tradecenter.subscription.model.enums.DeliveryFrequency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateSubscriptionRequest {

    @NotNull
    private Long productId;

    @NotNull
    private DeliveryFrequency frequency;

    @NotNull
    private Long shippingAddressId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public DeliveryFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(DeliveryFrequency frequency) {
        this.frequency = frequency;
    }

    public Long getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(Long shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
