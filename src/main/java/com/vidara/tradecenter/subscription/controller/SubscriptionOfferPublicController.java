package com.vidara.tradecenter.subscription.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.subscription.dto.response.SubscriptionOfferPublicResponse;
import com.vidara.tradecenter.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription-offers")
public class SubscriptionOfferPublicController {

    private final SubscriptionService subscriptionService;

    public SubscriptionOfferPublicController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<SubscriptionOfferPublicResponse>> getForProduct(
            @PathVariable Long productId) {
        SubscriptionOfferPublicResponse dto = subscriptionService.getPublicOffer(productId);
        return ResponseEntity.ok(ApiResponse.success("Subscription offer retrieved", dto));
    }
}
