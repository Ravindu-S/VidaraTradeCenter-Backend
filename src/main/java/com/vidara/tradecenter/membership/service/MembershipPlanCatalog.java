package com.vidara.tradecenter.membership.service;

import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Display prices (LKR) — adjust as needed for your business. */
public final class MembershipPlanCatalog {

    private MembershipPlanCatalog() {
    }

    public static BigDecimal monthlyFee(MembershipPlan plan) {
        return switch (plan) {
            case STARTER -> new BigDecimal("2499");
            case PROFESSIONAL -> new BigDecimal("7499");
            case ENTERPRISE -> new BigDecimal("19999");
        };
    }

    /** Yearly ≈ 10× monthly (~17% off vs paying 12 months). */
    public static BigDecimal yearlyFee(MembershipPlan plan) {
        return monthlyFee(plan).multiply(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal displayPrice(MembershipPlan plan, MembershipBillingPeriod period) {
        return period == MembershipBillingPeriod.YEARLY ? yearlyFee(plan) : monthlyFee(plan);
    }
}
