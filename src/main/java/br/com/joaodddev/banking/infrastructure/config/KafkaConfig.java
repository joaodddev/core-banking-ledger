package br.com.joaodddev.banking.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("!test")
public class KafkaConfig {

    @Bean
    public NewTopic bankingDomainEventsTopic() {
        return TopicBuilder.name("banking.domain.events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}