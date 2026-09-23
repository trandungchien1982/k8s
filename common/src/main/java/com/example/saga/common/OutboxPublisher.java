package com.example.saga.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxEventRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.publish-delay-ms:1000}")
    @Transactional
    public void publishPending() {
        for (OutboxEvent event : repository.findTop50ByPublishedFalseOrderByCreatedAtAsc()) {
            try {
                kafkaTemplate.send(SagaConstants.EVENTS_TOPIC, event.getAggregateId(), event.getPayload()).get();
                event.markPublished();
                log.info("Published outbox event {} type={}", event.getId(), event.getEventType());
            } catch (Exception e) {
                log.warn("Outbox event {} publish failed; it will be retried", event.getId(), e);
                break;
            }
        }
    }
}
