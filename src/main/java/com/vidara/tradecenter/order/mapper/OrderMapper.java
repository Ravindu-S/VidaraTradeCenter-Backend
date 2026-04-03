package com.vidara.tradecenter.order.mapper;

import com.vidara.tradecenter.order.dto.OrderHistoryResponse;
import com.vidara.tradecenter.order.dto.OrderStatusResponse;
import com.vidara.tradecenter.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderHistoryResponse toHistoryResponse(Order order) {
        if (order == null) {
            return null;
        }

        OrderHistoryResponse response = new OrderHistoryResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus() != null ? order.getOrderStatus().name() : null,
                order.getItemCount()
        );
        response.setPaymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null);
        return response;
    }

    public OrderStatusResponse toStatusResponse(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderStatusResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderStatus() != null ? order.getOrderStatus().name() : null,
                order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null,
                order.getOrderDate(),
                order.getUpdatedAt()
        );
    }
}
