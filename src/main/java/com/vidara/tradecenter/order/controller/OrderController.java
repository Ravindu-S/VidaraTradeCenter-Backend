package com.vidara.tradecenter.order.controller;

import com.vidara.tradecenter.common.dto.ApiResponse;
import com.vidara.tradecenter.common.exception.BadRequestException;
import com.vidara.tradecenter.common.exception.ResourceNotFoundException;
import com.vidara.tradecenter.order.dto.OrderListResponse;
import com.vidara.tradecenter.order.model.Order;
import com.vidara.tradecenter.order.repository.OrderRepository;
import com.vidara.tradecenter.security.CurrentUser;
import com.vidara.tradecenter.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{orderNumber}")
    @Transactional
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrder(
            @CurrentUser CustomUserDetails currentUser,
            @PathVariable String orderNumber) {

        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Order does not belong to this user");
        }

        OrderListResponse response = OrderListResponse.fromEntityDetailed(order);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", response));
    }
}
