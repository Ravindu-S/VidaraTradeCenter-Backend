package com.vidara.tradecenter.membership.dto;

import jakarta.validation.constraints.NotBlank;

public class MembershipReconcileRequest {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
