package com.vidara.tradecenter.membership.service;

import com.vidara.tradecenter.membership.model.enums.MembershipPlan;
import com.vidara.tradecenter.product.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Bulk quantity tiers + membership discount on catalog (retail) unit price.
 * Discounts multiply; combined discount is capped at 35%.
 */
public final class MembershipPricingCalculator {

    public static final BigDecimal MAX_COMBINED_DISCOUNT_PERCENT = new BigDecimal("35");

    private MembershipPricingCalculator() {
    }

    public static BigDecimal retailUnitPrice(Product product) {
        BigDecimal basePrice = product.getBasePrice();
        BigDecimal salePrice = product.getSalePrice();
        if (salePrice != null && basePrice != null && salePrice.compareTo(basePrice) < 0) {
            return salePrice;
        }
        return basePrice != null ? basePrice : BigDecimal.ZERO;
    }

    /** Bulk tier for a single line item quantity (5+). */
    public static int bulkDiscountPercent(int quantity) {
        if (quantity < 5) {
            return 0;
        }
        if (quantity <= 9) {
            return 3;
        }
        if (quantity <= 24) {
            return 7;
        }
        if (quantity <= 49) {
            return 10;
        }
        return 15;
    }

    public static int membershipProductDiscountPercent(MembershipPlan plan) {
        if (plan == null) {
            return 0;
        }
        return switch (plan) {
            case STARTER -> 5;
            case PROFESSIONAL -> 12;
            case ENTERPRISE -> 20;
        };
    }

    public static BigDecimal combinedDiscountPercent(int bulkPercent, int membershipPercent) {
        BigDecimal b = toFraction(bulkPercent);
        BigDecimal m = toFraction(membershipPercent);
        BigDecimal factor = BigDecimal.ONE.subtract(b).multiply(BigDecimal.ONE.subtract(m));
        return BigDecimal.ONE.subtract(factor).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal toFraction(int percent) {
        return BigDecimal.valueOf(percent).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
    }

    /**
     * Per-unit price after bulk + membership (multiplicative), capped at 35% total discount off retail.
     */
    public static BigDecimal effectiveUnitPrice(BigDecimal retailUnit, int quantity, MembershipPlan plan) {
        int bulk = bulkDiscountPercent(quantity);
        int mem = membershipProductDiscountPercent(plan);
        BigDecimal factor = BigDecimal.ONE.subtract(toFraction(bulk)).multiply(BigDecimal.ONE.subtract(toFraction(mem)));
        BigDecimal discountPercent = BigDecimal.ONE.subtract(factor).multiply(BigDecimal.valueOf(100));
        if (discountPercent.compareTo(MAX_COMBINED_DISCOUNT_PERCENT) > 0) {
            factor = BigDecimal.ONE.subtract(toFraction(35));
        }
        return retailUnit.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal effectiveLineTotal(BigDecimal retailUnit, int quantity, MembershipPlan plan) {
        return effectiveUnitPrice(retailUnit, quantity, plan).multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
