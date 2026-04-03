package com.vidara.tradecenter.order.dto;

import java.time.LocalDateTime;

public class OrderHistoryFilterRequest {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String search;

    public OrderHistoryFilterRequest() {
    }

    public OrderHistoryFilterRequest(LocalDateTime startDate, LocalDateTime endDate, String status, String search) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.search = search;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
