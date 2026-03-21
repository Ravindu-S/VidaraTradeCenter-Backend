package com.vidara.tradecenter.checkout.service;

import com.vidara.tradecenter.common.exception.BadRequestException;
import com.vidara.tradecenter.common.exception.ResourceNotFoundException;
import com.vidara.tradecenter.checkout.dto.*;
import com.vidara.tradecenter.order.model.Order;
import com.vidara.tradecenter.order.model.OrderItem;
import com.vidara.tradecenter.order.model.ShippingAddress;
import com.vidara.tradecenter.order.model.enums.PaymentStatus;
import com.vidara.tradecenter.order.repository.OrderRepository;
import com.vidara.tradecenter.product.model.Product;
import com.vidara.tradecenter.product.model.enums.ProductStatus;
import com.vidara.tradecenter.product.repository.ProductRepository;
import com.vidara.tradecenter.user.model.Address;
import com.vidara.tradecenter.user.model.User;
import com.vidara.tradecenter.user.repository.AddressRepository;
import com.vidara.tradecenter.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CheckoutService(ProductRepository productRepository,
                            AddressRepository addressRepository,
                            UserRepository userRepository,
                            OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public CheckoutPreviewResponse preview(Long userId, CheckoutPreviewRequest request) {
        ComputedCheckout computed = compute(userId, request.getShippingAddressId(), request.getItems());

        CheckoutPreviewResponse response = new CheckoutPreviewResponse();
        response.setSubtotal(computed.subtotal);
        response.setTax(BigDecimal.ZERO);
        response.setShippingCost(BigDecimal.ZERO);
        response.setTotalAmount(computed.totalAmount);
        response.setItems(toPreviewItems(computed.lines));
        return response;
    }

    @Transactional
    public CheckoutOrderResponse placeOrder(Long userId, CheckoutOrderRequest request) {
        ComputedCheckout computed = compute(userId, request.getShippingAddressId(), request.getItems());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        String orderNumber = generateUniqueOrderNumber();
        Order order = new Order(orderNumber, user, computed.subtotal, computed.totalAmount);
        order.setTax(BigDecimal.ZERO);
        order.setShippingCost(BigDecimal.ZERO);
        order.setPaymentStatus(PaymentStatus.PENDING);

        for (ComputedLine line : computed.lines) {
            OrderItem item = new OrderItem(
                    line.product,
                    line.productName,
                    line.quantity,
                    line.unitPrice,
                    line.totalPrice
            );
            order.addItem(item);
        }

        ShippingAddress shippingAddress = new ShippingAddress(
                computed.shippingAddress.getRecipientName(),
                computed.shippingAddress.getPhone(),
                computed.shippingAddress.getStreet(),
                computed.shippingAddress.getCity(),
                computed.shippingAddress.getState(),
                computed.shippingAddress.getZipCode()
        );
        shippingAddress.setCountry(computed.shippingAddress.getCountry());
        order.setShippingDetails(shippingAddress);

        Order saved = orderRepository.save(order);

        CheckoutOrderResponse response = new CheckoutOrderResponse();
        response.setOrderNumber(saved.getOrderNumber());
        response.setTotalAmount(saved.getTotalAmount());
        return response;
    }

    private List<CheckoutPreviewLineItemResponse> toPreviewItems(List<ComputedLine> lines) {
        List<CheckoutPreviewLineItemResponse> result = new ArrayList<>();
        for (ComputedLine line : lines) {
            CheckoutPreviewLineItemResponse dto = new CheckoutPreviewLineItemResponse();
            dto.setProductId(line.product.getId());
            dto.setProductName(line.productName);
            dto.setQuantity(line.quantity);
            dto.setUnitPrice(line.unitPrice);
            dto.setTotalPrice(line.totalPrice);
            result.add(dto);
        }
        return result;
    }

    private ComputedCheckout compute(Long userId,
                                        Long shippingAddressId,
                                        List<CheckoutItemRequest> itemRequests) {
        if (shippingAddressId == null) {
            throw new BadRequestException("shippingAddressId is required");
        }
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new BadRequestException("At least one item is required");
        }

        Address shippingAddress = addressRepository.findByIdAndUserId(shippingAddressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", shippingAddressId));

        // DB schema requires state and postal_code to be non-null.
        if (shippingAddress.getState() == null || shippingAddress.getState().trim().isEmpty()) {
            throw new BadRequestException("State is required in shipping address");
        }
        if (shippingAddress.getZipCode() == null || shippingAddress.getZipCode().trim().isEmpty()) {
            throw new BadRequestException("Postal code is required in shipping address");
        }

        List<ComputedLine> lines = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CheckoutItemRequest req : itemRequests) {
            if (req.getProductId() == null) {
                throw new BadRequestException("productId is required for each item");
            }
            if (req.getQuantity() == null || req.getQuantity() < 1) {
                throw new BadRequestException("quantity must be >= 1");
            }

            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", req.getProductId()));

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new BadRequestException("Product is not available: " + product.getName());
            }

            int quantity = req.getQuantity();
            int availableStock = product.getStock() == null ? 0 : product.getStock();
            if (availableStock < quantity) {
                throw new BadRequestException("Insufficient stock for " + product.getName());
            }

            BigDecimal unitPrice = resolveUnitPrice(product);
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));

            subtotal = subtotal.add(totalPrice);
            lines.add(new ComputedLine(product, product.getName(), quantity, unitPrice, totalPrice));
        }

        ComputedCheckout computed = new ComputedCheckout();
        computed.shippingAddress = shippingAddress;
        computed.lines = lines;
        computed.subtotal = subtotal;
        computed.tax = BigDecimal.ZERO;
        computed.shippingCost = BigDecimal.ZERO;
        computed.totalAmount = subtotal;
        return computed;
    }

    private BigDecimal resolveUnitPrice(Product product) {
        BigDecimal basePrice = product.getBasePrice();
        BigDecimal salePrice = product.getSalePrice();

        if (salePrice != null && basePrice != null && salePrice.compareTo(basePrice) < 0) {
            return salePrice;
        }
        return basePrice;
    }

    private String generateUniqueOrderNumber() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String candidate = "OD" + System.currentTimeMillis() + "-" + randomSuffix();
            if (!orderRepository.existsByOrderNumber(candidate)) {
                return candidate;
            }
        }
        throw new BadRequestException("Could not generate a unique order number");
    }

    private String randomSuffix() {
        // Keep under 50 chars total even with timestamp.
        String u = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return u.substring(0, Math.min(8, u.length()));
    }

    private static class ComputedLine {
        private final Product product;
        private final String productName;
        private final int quantity;
        private final BigDecimal unitPrice;
        private final BigDecimal totalPrice;

        private ComputedLine(Product product,
                              String productName,
                              int quantity,
                              BigDecimal unitPrice,
                              BigDecimal totalPrice) {
            this.product = product;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
        }
    }

    private static class ComputedCheckout {
        private Address shippingAddress;
        private List<ComputedLine> lines;
        private BigDecimal subtotal;
        private BigDecimal tax;
        private BigDecimal shippingCost;
        private BigDecimal totalAmount;
    }
}

