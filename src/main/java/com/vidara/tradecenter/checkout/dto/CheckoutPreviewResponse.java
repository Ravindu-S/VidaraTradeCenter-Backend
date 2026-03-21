package com.vidara.tradecenter.checkout.dto;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutPreviewResponse {

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal shippingCost;
    private BigDecimal totalAmount;

    private List<CheckoutPreviewLineItemResponse> items;

    public CheckoutPreviewResponse() {
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CheckoutPreviewLineItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CheckoutPreviewLineItemResponse> items) {
        this.items = items;
    }
}

