package com.vidara.tradecenter.subscription.util;

import com.vidara.tradecenter.subscription.model.enums.DeliveryFrequency;

import java.time.LocalDate;

public final class SubscriptionSchedule {

    private SubscriptionSchedule() {
    }

    public static LocalDate nextFrom(LocalDate from, DeliveryFrequency frequency) {
        return switch (frequency) {
            case WEEKLY -> from.plusWeeks(1);
            case BIWEEKLY -> from.plusWeeks(2);
            case MONTHLY -> from.plusMonths(1);
            case QUARTERLY -> from.plusMonths(3);
        };
    }
}
