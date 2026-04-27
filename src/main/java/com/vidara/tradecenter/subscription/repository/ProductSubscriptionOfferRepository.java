package com.vidara.tradecenter.subscription.repository;

import com.vidara.tradecenter.subscription.model.ProductSubscriptionOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductSubscriptionOfferRepository extends JpaRepository<ProductSubscriptionOffer, Long> {

    Optional<ProductSubscriptionOffer> findByProductId(Long productId);

    List<ProductSubscriptionOffer> findByProductIdIn(Collection<Long> productIds);

    @Query("""
            SELECT COUNT(o) > 0 FROM ProductSubscriptionOffer o
            WHERE o.product.id = :productId AND o.enabled = true
            """)
    boolean existsEnabledByProductId(@Param("productId") Long productId);
}
