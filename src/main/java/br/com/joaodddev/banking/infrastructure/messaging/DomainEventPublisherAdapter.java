package br.com.joaodddev.banking.infrastructure.messaging;

import br.com.joaodddev.banking.application.port.out.DomainEventPublisher;
import br.com.joaodddev.banking.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainEventPublisherAdapter implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisherAdapter.class);
    private static final String TOPIC = "banking.domain.events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public DomainEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(event -> {
            kafkaTemplate.send(TOPIC, event.eventId().toString(), event);
            log.info("Published event [{}] id={}", event.eventType(), event.eventId());
        });
    }
}