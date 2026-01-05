package ru.example.task_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Map;
import java.util.UUID;
import org.openapitools.model.TaskEvent;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    DefaultKafkaProducerFactory<UUID, TaskEvent> taskEventProducerFactory(
            KafkaProperties properties) {
        Map<String, Object> producerProperties = properties.buildProducerProperties(null);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    KafkaTemplate<UUID, TaskEvent>  taskEventKafkaTemplate(
            DefaultKafkaProducerFactory<UUID, TaskEvent> taskEventProducerFactory
    ) {
        return new KafkaTemplate<>(taskEventProducerFactory);
    }

    @Bean
    public KafkaListenerContainerFactory<?> taskEventListenerFactory(
            ConsumerFactory<UUID, TaskEvent> taskEventConsumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<UUID, TaskEvent>();
        factory.setConsumerFactory(taskEventConsumerFactory);
        factory.setBatchListener(false);
        return factory;
    }

}
