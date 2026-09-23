package com.example.saga.common;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
public class OutboxEvent {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String aggregateType;
    @Column(nullable = false)
    private String aggregateId;
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false, columnDefinition = "text")
    private String payload;
    @Column(nullable = false)
    private Instant createdAt;
    @Column(nullable = false)
    private boolean published;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateType, String aggregateId, String eventType, String payload) {
        this.id = UUID.randomUUID();
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.published = false;
    }

    public UUID getId() { return id; }
    public String getAggregateType() { return aggregateType; }
    public String getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isPublished() { return published; }
    public void markPublished() { this.published = true; }
}
