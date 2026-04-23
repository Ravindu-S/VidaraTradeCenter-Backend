package com.vidara.tradecenter.subscription.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.common.dto.PagedResponse;
import com.vidara.tradecenter.subscription.dto.request.AdminUpsertSubscriptionOfferRequest;
import com.vidara.tradecenter.subscription.dto.response.AdminSubscriptionOfferRowResponse;
import com.vidara.tradecenter.subscription.dto.response.AdminSubscriptionRowResponse;
import com.vidara.tradecenter.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminSubscriptionController {

    private final SubscriptionService subscriptionService;

    public AdminSubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<ApiResponse<PagedResponse<AdminSubscriptionRowResponse>>> listSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                "Subscriptions retrieved",
                subscriptionService.adminListSubscriptions(page, size)));
    }

    @GetMapping("/subscription-offers")
    public ResponseEntity<ApiResponse<PagedResponse<AdminSubscriptionOfferRowResponse>>> listOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                "Subscription offers retrieved",
                subscriptionService.adminListOffers(page, size)));
    }

    @PutMapping("/subscription-offers/products/{productId}")
    public ResponseEntity<ApiResponse<AdminSubscriptionOfferRowResponse>> upsertOffer(
            @PathVariable Long productId,
            @Valid @RequestBody AdminUpsertSubscriptionOfferRequest request) {
        AdminSubscriptionOfferRowResponse row = subscriptionService.adminUpsertOffer(productId, request);
        return ResponseEntity.ok(ApiResponse.success("Subscription offer saved", row));
    }
}
