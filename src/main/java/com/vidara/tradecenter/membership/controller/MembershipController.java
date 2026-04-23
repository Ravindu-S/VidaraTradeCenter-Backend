package com.vidara.tradecenter.membership.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.membership.dto.MembershipCheckoutResponse;
import com.vidara.tradecenter.membership.dto.MembershipMeResponse;
import com.vidara.tradecenter.membership.dto.MembershipReconcileRequest;
import com.vidara.tradecenter.membership.dto.MembershipPlanResponse;
import com.vidara.tradecenter.membership.dto.SubscribeMembershipRequest;
import com.vidara.tradecenter.membership.service.MembershipCheckoutService;
import com.vidara.tradecenter.membership.service.MembershipService;
import com.vidara.tradecenter.security.CurrentUser;
import com.vidara.tradecenter.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipService membershipService;
    private final MembershipCheckoutService membershipCheckoutService;

    public MembershipController(MembershipService membershipService,
            MembershipCheckoutService membershipCheckoutService) {
        this.membershipService = membershipService;
        this.membershipCheckoutService = membershipCheckoutService;
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<MembershipPlanResponse>>> plans() {
        return ResponseEntity.ok(ApiResponse.success("Plans retrieved", membershipService.listPlans()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MembershipMeResponse>> me(@CurrentUser CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success("Membership retrieved", membershipService.getMe(user.getId())));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<MembershipCheckoutResponse>> checkout(
            @CurrentUser CustomUserDetails user,
            @Valid @RequestBody SubscribeMembershipRequest request) {
        MembershipCheckoutResponse dto = membershipCheckoutService.createCheckout(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Proceed to payment to activate your membership", dto));
    }

    /**
     * Sandbox-only: marks a pending MS checkout paid after the browser completes PayHere, when {@code notify_url}
     * cannot be reached (typical on localhost). Disabled unless {@code payhere.membership-sandbox-reconcile-enabled=true}.
     */
    @PostMapping("/reconcile-sandbox")
    public ResponseEntity<ApiResponse<MembershipMeResponse>> reconcileSandbox(
            @CurrentUser CustomUserDetails user,
            @Valid @RequestBody MembershipReconcileRequest request) {
        membershipCheckoutService.reconcileSandboxMembershipIfPending(user.getId(), request.getOrderNumber());
        MembershipMeResponse dto = membershipService.getMe(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Membership updated", dto));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<MembershipMeResponse>> cancel(@CurrentUser CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success("Membership cancelled", membershipService.cancel(user.getId())));
    }
}
