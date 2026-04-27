package com.vidara.tradecenter.subscription.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.security.CurrentUser;
import com.vidara.tradecenter.security.CustomUserDetails;
import com.vidara.tradecenter.subscription.dto.request.CreateSubscriptionRequest;
import com.vidara.tradecenter.subscription.dto.request.UpdateSubscriptionFrequencyRequest;
import com.vidara.tradecenter.subscription.dto.response.SubscriptionResponse;
import com.vidara.tradecenter.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<SubscriptionResponse>>> mine(@CurrentUser CustomUserDetails user) {
        List<SubscriptionResponse> list = subscriptionService.listMine(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Subscriptions retrieved", list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriptionResponse>> create(
            @CurrentUser CustomUserDetails user,
            @Valid @RequestBody CreateSubscriptionRequest request) {
        SubscriptionResponse created = subscriptionService.create(user.getId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subscription created successfully", created));
    }

    @PatchMapping("/{id}/frequency")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> updateFrequency(
            @CurrentUser CustomUserDetails user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubscriptionFrequencyRequest request) {
        SubscriptionResponse updated = subscriptionService.updateFrequency(user.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Subscription frequency updated", updated));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> cancel(
            @CurrentUser CustomUserDetails user,
            @PathVariable Long id) {
        SubscriptionResponse cancelled = subscriptionService.cancel(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Subscription cancelled", cancelled));
    }
}
