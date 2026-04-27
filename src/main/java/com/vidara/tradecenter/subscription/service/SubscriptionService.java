package com.vidara.tradecenter.subscription.service;

import com.vidara.tradecenter.checkout.dto.CheckoutResponse;
import com.vidara.tradecenter.checkout.service.CheckoutService;
import com.vidara.tradecenter.common.dto.PagedResponse;
import com.vidara.tradecenter.common.exception.BadRequestException;
import com.vidara.tradecenter.common.exception.ResourceNotFoundException;
import com.vidara.tradecenter.product.model.Product;
import com.vidara.tradecenter.product.model.enums.ProductStatus;
import com.vidara.tradecenter.product.repository.ProductRepository;
import com.vidara.tradecenter.subscription.dto.request.AdminUpsertSubscriptionOfferRequest;
import com.vidara.tradecenter.subscription.dto.request.CreateSubscriptionRequest;
import com.vidara.tradecenter.subscription.dto.request.UpdateSubscriptionFrequencyRequest;
import com.vidara.tradecenter.subscription.dto.response.AdminSubscriptionOfferRowResponse;
import com.vidara.tradecenter.subscription.dto.response.AdminSubscriptionRowResponse;
import com.vidara.tradecenter.subscription.dto.response.SubscriptionOfferPublicResponse;
import com.vidara.tradecenter.subscription.dto.response.SubscriptionResponse;
import com.vidara.tradecenter.subscription.model.ProductSubscriptionOffer;
import com.vidara.tradecenter.subscription.model.Subscription;
import com.vidara.tradecenter.subscription.model.enums.DeliveryFrequency;
import com.vidara.tradecenter.subscription.model.enums.SubscriptionStatus;
import com.vidara.tradecenter.subscription.repository.ProductSubscriptionOfferRepository;
import com.vidara.tradecenter.subscription.repository.SubscriptionRepository;
import com.vidara.tradecenter.subscription.util.SubscriptionPricing;
import com.vidara.tradecenter.subscription.util.SubscriptionSchedule;
import com.vidara.tradecenter.user.model.Address;
import com.vidara.tradecenter.user.model.User;
import com.vidara.tradecenter.user.repository.AddressRepository;
import com.vidara.tradecenter.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final ProductSubscriptionOfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CheckoutService checkoutService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
            ProductSubscriptionOfferRepository offerRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            AddressRepository addressRepository,
            CheckoutService checkoutService) {
        this.subscriptionRepository = subscriptionRepository;
        this.offerRepository = offerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.checkoutService = checkoutService;
    }

    public SubscriptionOfferPublicResponse getPublicOffer(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        SubscriptionOfferPublicResponse dto = new SubscriptionOfferPublicResponse();
        dto.setProductId(productId);
        dto.setRetailUnitPrice(SubscriptionPricing.retailUnitPrice(product));

        Optional<ProductSubscriptionOffer> offerOpt = offerRepository.findByProductId(productId);
        if (offerOpt.isEmpty() || !offerOpt.get().isEnabled()) {
            dto.setSubscriptionEnabled(false);
            dto.setDiscountPercent(BigDecimal.ZERO);
            dto.setSubscriptionUnitPrice(dto.getRetailUnitPrice());
            return dto;
        }

        ProductSubscriptionOffer offer = offerOpt.get();
        dto.setSubscriptionEnabled(true);
        dto.setDiscountPercent(offer.getDiscountPercent());
        dto.setSubscriptionUnitPrice(SubscriptionPricing.subscriptionUnitPrice(product, offer.getDiscountPercent()));
        return dto;
    }

    @Transactional
    public SubscriptionResponse create(Long userId, CreateSubscriptionRequest request) {
        if (!offerRepository.existsEnabledByProductId(request.getProductId())) {
            throw new BadRequestException("This product is not available for subscription");
        }

        if (subscriptionRepository.existsByUserIdAndProductIdAndStatus(
                userId, request.getProductId(), SubscriptionStatus.ACTIVE)) {
            throw new BadRequestException("You already have an active subscription for this product");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException("Product is not available for subscription");
        }

        ProductSubscriptionOffer offer = offerRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new BadRequestException("This product is not available for subscription"));

        int qty = request.getQuantity();
        int stock = product.getStock() == null ? 0 : product.getStock();
        if (stock < qty) {
            throw new BadRequestException("Insufficient stock to start this subscription");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = addressRepository.findByIdAndUserId(request.getShippingAddressId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getShippingAddressId()));

        BigDecimal discount = offer.getDiscountPercent();
        BigDecimal unit = SubscriptionPricing.subscriptionUnitPrice(product, discount);
        LocalDate today = LocalDate.now();
        LocalDate nextBilling = SubscriptionSchedule.nextFrom(today, request.getFrequency());

        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setProduct(product);
        sub.setShippingAddress(address);
        sub.setFrequency(request.getFrequency());
        sub.setQuantity(qty);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setUnitPriceSnapshot(unit);
        sub.setDiscountPercentApplied(discount);
        sub.setNextBillingDate(nextBilling);

        Subscription saved = subscriptionRepository.save(sub);
        return toResponse(saved);
    }

    public List<SubscriptionResponse> listMine(Long userId) {
        return subscriptionRepository.findByUserIdWithDetails(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponse updateFrequency(Long userId, Long subscriptionId,
            UpdateSubscriptionFrequencyRequest request) {
        Subscription sub = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Only active subscriptions can be updated");
        }

        sub.setFrequency(request.getFrequency());
        sub.setNextBillingDate(SubscriptionSchedule.nextFrom(LocalDate.now(), request.getFrequency()));
        return toResponse(subscriptionRepository.save(sub));
    }

    @Transactional
    public SubscriptionResponse cancel(Long userId, Long subscriptionId) {
        Subscription sub = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", "id", subscriptionId));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Subscription is already cancelled");
        }

        sub.setStatus(SubscriptionStatus.CANCELLED);
        sub.setCancelledAt(LocalDateTime.now());
        return toResponse(subscriptionRepository.save(sub));
    }

    @Transactional(TxType.REQUIRED)
    public void processDueRenewals() {
        List<Subscription> due = subscriptionRepository.findDueActive(SubscriptionStatus.ACTIVE, LocalDate.now());
        for (Subscription s : due) {
            try {
                CheckoutResponse placed = checkoutService.placeSingleItemOrder(
                        s.getUser().getId(),
                        s.getProduct().getId(),
                        s.getQuantity(),
                        s.getShippingAddress().getId(),
                        s.getUnitPriceSnapshot());
                log.info("Subscription renewal order created: subscriptionId={}, orderNumber={}",
                        s.getId(), placed.getOrderNumber());
                s.setNextBillingDate(SubscriptionSchedule.nextFrom(LocalDate.now(), s.getFrequency()));
                subscriptionRepository.save(s);
            } catch (Exception e) {
                log.warn("Subscription renewal failed for id={}: {}", s.getId(), e.getMessage());
                s.setNextBillingDate(LocalDate.now().plusDays(1));
                subscriptionRepository.save(s);
            }
        }
    }

    public PagedResponse<AdminSubscriptionRowResponse> adminListSubscriptions(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        Page<Subscription> p = subscriptionRepository.findAllForAdmin(pageable);
        List<AdminSubscriptionRowResponse> rows = p.getContent().stream()
                .map(this::toAdminRow)
                .collect(Collectors.toList());
        return PagedResponse.of(rows, p);
    }

    public PagedResponse<AdminSubscriptionOfferRowResponse> adminListOffers(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100));
        Page<Product> products = productRepository.findByStatus(ProductStatus.ACTIVE, pageable);
        List<Long> ids = products.getContent().stream().map(Product::getId).toList();
        Map<Long, ProductSubscriptionOffer> offerByProduct = offerRepository.findByProductIdIn(ids).stream()
                .collect(Collectors.toMap(o -> o.getProduct().getId(), Function.identity()));

        List<AdminSubscriptionOfferRowResponse> rows = products.getContent().stream()
                .map(pr -> {
                    AdminSubscriptionOfferRowResponse r = new AdminSubscriptionOfferRowResponse();
                    r.setProductId(pr.getId());
                    r.setProductName(pr.getName());
                    r.setSku(pr.getSku());
                    ProductSubscriptionOffer o = offerByProduct.get(pr.getId());
                    if (o == null) {
                        r.setOfferConfigured(false);
                        r.setSubscriptionEnabled(false);
                        r.setDiscountPercent(BigDecimal.ZERO);
                    } else {
                        r.setOfferConfigured(true);
                        r.setSubscriptionEnabled(o.isEnabled());
                        r.setDiscountPercent(o.getDiscountPercent());
                    }
                    return r;
                })
                .collect(Collectors.toList());
        return PagedResponse.of(rows, products);
    }

    @Transactional
    public AdminSubscriptionOfferRowResponse adminUpsertOffer(Long productId, AdminUpsertSubscriptionOfferRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        ProductSubscriptionOffer offer = offerRepository.findByProductId(productId)
                .orElseGet(() -> {
                    ProductSubscriptionOffer o = new ProductSubscriptionOffer();
                    o.setProduct(product);
                    return o;
                });

        offer.setEnabled(Boolean.TRUE.equals(req.getEnabled()));
        offer.setDiscountPercent(req.getDiscountPercent() == null ? BigDecimal.ZERO : req.getDiscountPercent());
        ProductSubscriptionOffer saved = offerRepository.save(offer);

        AdminSubscriptionOfferRowResponse r = new AdminSubscriptionOfferRowResponse();
        r.setProductId(productId);
        r.setProductName(product.getName());
        r.setSku(product.getSku());
        r.setOfferConfigured(true);
        r.setSubscriptionEnabled(saved.isEnabled());
        r.setDiscountPercent(saved.getDiscountPercent());
        return r;
    }

    private SubscriptionResponse toResponse(Subscription s) {
        SubscriptionResponse r = new SubscriptionResponse();
        r.setId(s.getId());
        r.setProductId(s.getProduct().getId());
        r.setProductName(s.getProduct().getName());
        r.setFrequency(s.getFrequency());
        r.setQuantity(s.getQuantity());
        r.setStatus(s.getStatus());
        r.setUnitPriceSnapshot(s.getUnitPriceSnapshot());
        r.setDiscountPercentApplied(s.getDiscountPercentApplied());
        r.setNextBillingDate(s.getNextBillingDate());
        r.setCancelledAt(s.getCancelledAt());
        r.setShippingAddressId(s.getShippingAddress().getId());
        return r;
    }

    private AdminSubscriptionRowResponse toAdminRow(Subscription s) {
        AdminSubscriptionRowResponse r = new AdminSubscriptionRowResponse();
        r.setId(s.getId());
        r.setUserId(s.getUser().getId());
        r.setCustomerEmail(s.getUser().getEmail());
        r.setProductId(s.getProduct().getId());
        r.setProductName(s.getProduct().getName());
        r.setFrequency(s.getFrequency());
        r.setQuantity(s.getQuantity());
        r.setStatus(s.getStatus());
        r.setUnitPriceSnapshot(s.getUnitPriceSnapshot());
        r.setDiscountPercentApplied(s.getDiscountPercentApplied());
        r.setNextBillingDate(s.getNextBillingDate());
        r.setCancelledAt(s.getCancelledAt());
        return r;
    }
}
