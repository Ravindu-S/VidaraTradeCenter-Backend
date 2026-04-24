package com.vidara.tradecenter.support.dto;

import com.vidara.tradecenter.support.model.SupportTicket;
import com.vidara.tradecenter.support.model.enums.TicketCategory;
import com.vidara.tradecenter.support.model.enums.TicketPriority;
import com.vidara.tradecenter.support.model.enums.TicketStatus;

import java.time.LocalDateTime;

public class SupportTicketResponse {

    private Long id;
    private String subject;
    private TicketCategory category;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // CONSTRUCTORS

    public SupportTicketResponse() {
    }

    public SupportTicketResponse(Long id, String subject, TicketCategory category,
                                  String description, TicketStatus status, TicketPriority priority,
                                  Long userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.subject = subject;
        this.category = category;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // STATIC FACTORY METHOD

    public static SupportTicketResponse fromEntity(SupportTicket ticket) {
        return new SupportTicketResponse(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getCategory(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getUser().getId(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }


    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
