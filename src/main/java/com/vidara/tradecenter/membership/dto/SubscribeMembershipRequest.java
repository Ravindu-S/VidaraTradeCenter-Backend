package com.vidara.tradecenter.membership.dto;

import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;
import jakarta.validation.constraints.NotNull;

public class SubscribeMembershipRequest {

    @NotNull
    private MembershipPlan plan;

    @NotNull
    private MembershipBillingPeriod billingPeriod;

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
