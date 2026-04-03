package com.vidara.tradecenter.order.service;

import com.vidara.tradecenter.common.dto.PagedResponse;
import com.vidara.tradecenter.order.dto.OrderHistoryFilterRequest;
import com.vidara.tradecenter.order.dto.OrderHistoryResponse;
import com.vidara.tradecenter.order.dto.OrderListResponse;
import com.vidara.tradecenter.order.dto.OrderStatusResponse;

public interface OrderService {

    PagedResponse<OrderHistoryResponse> getOrderHistory(
            Long userId,
            OrderHistoryFilterRequest filterRequest,
            int page,
            int size);

    OrderListResponse getOrderDetails(Long userId, Long orderId);
    OrderListResponse getOrderDetailsByOrderNumber(Long userId, String orderNumber);

    OrderStatusResponse getOrderStatus(Long userId, Long orderId);
}
