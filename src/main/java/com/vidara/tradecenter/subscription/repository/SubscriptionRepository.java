package com.vidara.tradecenter.subscription.repository;

import com.vidara.tradecenter.subscription.model.Subscription;
import com.vidara.tradecenter.subscription.model.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("""
            SELECT s FROM Subscription s
            JOIN FETCH s.product
            JOIN FETCH s.shippingAddress
            WHERE s.user.id = :userId
            ORDER BY s.createdAt DESC
            """)
    List<Subscription> findByUserIdWithDetails(@Param("userId") Long userId);

    Optional<Subscription> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT DISTINCT s FROM Subscription s
            JOIN FETCH s.product
            JOIN FETCH s.user
            JOIN FETCH s.shippingAddress
            WHERE s.status = :status AND s.nextBillingDate <= :date
            """)
    List<Subscription> findDueActive(@Param("status") SubscriptionStatus status, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"user", "product", "shippingAddress"})
    @Query("SELECT s FROM Subscription s ORDER BY s.createdAt DESC")
    Page<Subscription> findAllForAdmin(Pageable pageable);

    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, SubscriptionStatus status);
}
