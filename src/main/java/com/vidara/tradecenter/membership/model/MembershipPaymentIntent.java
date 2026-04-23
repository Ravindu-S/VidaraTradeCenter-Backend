package com.vidara.tradecenter.membership.model;

import com.vidara.tradecenter.common.base.BaseEntity;
import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipIntentStatus;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;
import com.vidara.tradecenter.user.model.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "membership_payment_intents", indexes = {
        @Index(name = "idx_membership_intents_user", columnList = "user_id"),
        @Index(name = "idx_membership_intents_status", columnList = "status")
})
public class MembershipPaymentIntent extends BaseEntity {

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 32)
    private MembershipPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false, length = 20)
    private MembershipBillingPeriod billingPeriod;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MembershipIntentStatus status = MembershipIntentStatus.PENDING;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MembershipPlan getPlan() {
        return plan;
    }

    public void setPlan(MembershipPlan plan) {
        this.plan = plan;
    }

    public MembershipBillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(MembershipBillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MembershipIntentStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipIntentStatus status) {
        this.status = status;
    }
}
