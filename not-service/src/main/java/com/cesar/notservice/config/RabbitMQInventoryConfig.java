package com.cesar.notservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQInventoryConfig {

    @Value("${rabbitmq.exchange.name}") private String exchangeName;

    @Value("${rabbitmq.inventory.queue.string.name}") private String inventoryQueueStringName;
    @Value("${rabbitmq.inventory.routing.key.string.name}") private String inventoryRoutingKey;

    @Value("${rabbitmq.inventory.queue.json.name}") private String inventoryQueueJSONName;
    @Value("${rabbitmq.inventory.routing.key.json.name}") private String inventoryJSONRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue inventoryStringQueue() {
        return new Queue(inventoryQueueStringName);
    }

    @Bean
    public Queue inventoryJSONQueue() {
        return new Queue(inventoryQueueJSONName);
    }

    @Bean
    public Binding inventoryStringBinding() {
        return BindingBuilder
                .bind(inventoryStringQueue())
                .to(exchange())
                .with(inventoryRoutingKey);
    }

    @Bean
    public Binding inventoryJSONBinding() {
        return BindingBuilder
                .bind(inventoryJSONQueue())
                .to(exchange())
                .with(inventoryJSONRoutingKey);
    }
}
