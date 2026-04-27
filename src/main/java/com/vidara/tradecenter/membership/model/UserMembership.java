package com.vidara.tradecenter.membership.model;

import com.vidara.tradecenter.common.base.BaseEntity;
import com.vidara.tradecenter.membership.model.enums.MembershipBillingPeriod;
import com.vidara.tradecenter.membership.model.enums.MembershipPlan;
import com.vidara.tradecenter.membership.model.enums.MembershipRecordStatus;
import com.vidara.tradecenter.user.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_memberships", indexes = {
        @Index(name = "idx_user_memberships_user", columnList = "user_id"),
        @Index(name = "idx_user_memberships_user_status", columnList = "user_id, status")
})
public class UserMembership extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 32)
    private MembershipPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_period", nullable = false, length = 20)
    private MembershipBillingPeriod billingPeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MembershipRecordStatus status = MembershipRecordStatus.ACTIVE;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

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

    public MembershipRecordStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipRecordStatus status) {
        this.status = status;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
