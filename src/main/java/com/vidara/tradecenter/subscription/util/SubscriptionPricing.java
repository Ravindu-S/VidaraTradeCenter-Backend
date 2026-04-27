package com.vidara.tradecenter.subscription.util;

import com.vidara.tradecenter.product.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SubscriptionPricing {

    private SubscriptionPricing() {
    }

    public static BigDecimal retailUnitPrice(Product product) {
        BigDecimal basePrice = product.getBasePrice();
        BigDecimal salePrice = product.getSalePrice();
        if (salePrice != null && basePrice != null && salePrice.compareTo(basePrice) < 0) {
            return salePrice;
        }
        return basePrice != null ? basePrice : BigDecimal.ZERO;
    }

    public static BigDecimal subscriptionUnitPrice(Product product, BigDecimal discountPercent) {
        BigDecimal retail = retailUnitPrice(product);
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return retail.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal factor = BigDecimal.ONE.subtract(
                discountPercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
        return retail.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
