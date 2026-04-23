package com.vidara.tradecenter.subscription.dto.request;

import com.vidara.tradecenter.subscription.model.enums.DeliveryFrequency;
import jakarta.validation.constraints.NotNull;

public class UpdateSubscriptionFrequencyRequest {

    @NotNull
    private DeliveryFrequency frequency;

    public DeliveryFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(DeliveryFrequency frequency) {
        this.frequency = frequency;
    }
}
