package com.example.saga.order;

import com.example.saga.common.SagaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SagaEventObserver {
    private static final Logger log = LoggerFactory.getLogger(SagaEventObserver.class);

    @KafkaListener(topics = SagaConstants.EVENTS_TOPIC, groupId = "order-service-demo-audit")
    public void onEvent(String payload) {
        log.info("Kafka saga event: {}", payload);
    }
}
