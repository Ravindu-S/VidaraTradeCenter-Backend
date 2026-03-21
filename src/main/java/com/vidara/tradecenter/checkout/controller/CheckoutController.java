package com.vidara.tradecenter.checkout.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.checkout.dto.*;
import com.vidara.tradecenter.checkout.service.CheckoutService;
import com.vidara.tradecenter.security.CurrentUser;
import com.vidara.tradecenter.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<CheckoutPreviewResponse>> preview(
            @CurrentUser CustomUserDetails currentUser,
            @Valid @RequestBody CheckoutPreviewRequest request) {
        CheckoutPreviewResponse response =
                checkoutService.preview(currentUser.getId(), request);
        return ResponseEntity
                .ok(ApiResponse.success("Checkout preview computed successfully", response));
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<CheckoutOrderResponse>> placeOrder(
            @CurrentUser CustomUserDetails currentUser,
            @Valid @RequestBody CheckoutOrderRequest request) {
        CheckoutOrderResponse response =
                checkoutService.placeOrder(currentUser.getId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }
}

