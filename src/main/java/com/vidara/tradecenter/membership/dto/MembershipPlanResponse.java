package com.vidara.tradecenter.membership.dto;

import com.vidara.tradecenter.membership.model.enums.MembershipPlan;

import java.math.BigDecimal;
import java.util.List;

public class MembershipPlanResponse {

    private MembershipPlan plan;
    private String title;
    private BigDecimal monthlyPrice;
    private BigDecimal yearlyPrice;
    private int productDiscountPercent;
    private String subtitle;
    private boolean mostPopular;
    private List<String> features;

    public MembershipPlan getPlan() {
        return plan;
    }

    public void setPlan(MembershipPlan plan) {
        this.plan = plan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public BigDecimal getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(BigDecimal yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public int getProductDiscountPercent() {
        return productDiscountPercent;
    }

    public void setProductDiscountPercent(int productDiscountPercent) {
        this.productDiscountPercent = productDiscountPercent;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean isMostPopular() {
        return mostPopular;
    }

    public void setMostPopular(boolean mostPopular) {
        this.mostPopular = mostPopular;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
