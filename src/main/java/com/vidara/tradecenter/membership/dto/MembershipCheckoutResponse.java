package com.vidara.tradecenter.membership.dto;

import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;

import java.math.BigDecimal;

public class MembershipCheckoutResponse {

    private String orderNumber;
    private BigDecimal amount;
    private MembershipPlan plan;
    private MembershipBillingPeriod billingPeriod;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MembershipPlan getPlan() {
        return plan;
    }

    public void setPlan(MembershipPlan plan) {
        this.plan = plan;
    }

    public MembershipBillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(MembershipBillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }
}
