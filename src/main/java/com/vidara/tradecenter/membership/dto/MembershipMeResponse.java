package com.vidara.tradecenter.membership.dto;

import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;

public class MembershipMeResponse {

    private boolean active;
    private MembershipPlan plan;
    private MembershipBillingPeriod billingPeriod;
    private int productDiscountPercent;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getProductDiscountPercent() {
        return productDiscountPercent;
    }

    public void setProductDiscountPercent(int productDiscountPercent) {
        this.productDiscountPercent = productDiscountPercent;
    }
}
