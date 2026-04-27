package com.vidara.tradecenter.membership.repository;

import com.vidara.tradecenter.membership.model.MembershipPaymentIntent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipPaymentIntentRepository extends JpaRepository<MembershipPaymentIntent, Long> {

    Optional<MembershipPaymentIntent> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);
}
