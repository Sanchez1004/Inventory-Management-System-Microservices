package com.cesar.notservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ exchanges, queues, and bindings.
 *
 * <p>This class demonstrates basic RabbitMQ configuration using Spring Boot and can be customized
 * to fit specific requirements.</p>
 */
@Configuration
public class RabbitMQInventoryConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.inventory.queue.string.name}")
    private String inventoryQueueStringName;

    @Value("${rabbitmq.inventory.routing.key.string.name}")
    private String inventoryRoutingKey;

    @Value("${rabbitmq.inventory.queue.json.name}")
    private String inventoryQueueJSONName;

    @Value("${rabbitmq.inventory.routing.key.json.name}")
    private String inventoryJSONRoutingKey;

    /**
     * Defines a {@link TopicExchange} bean with the name specified by the {@code rabbitmq.exchange.name} property.
     *
     * @return the configured {@code TopicExchange} bean
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * Defines a {@link Queue} bean for string messages with the name specified by the
     * {@code rabbitmq.inventory.queue.string.name} property.
     *
     * @return the configured {@code Queue} bean for string messages
     */
    @Bean
    public Queue inventoryStringQueue() {
        return new Queue(inventoryQueueStringName);
    }

    /**
     * Defines a {@link Queue} bean for JSON messages with the name specified by the
     * {@code rabbitmq.inventory.queue.json.name} property.
     *
     * @return the configured {@code Queue} bean for JSON messages
     */
    @Bean
    public Queue inventoryJSONQueue() {
        return new Queue(inventoryQueueJSONName);
    }

    /**
     * Defines a {@link Binding} bean that binds the string queue to the exchange using the routing key
     * specified by the {@code rabbitmq.inventory.routing.key.string.name} property.
     *
     * @return the configured {@code Binding} bean for the string queue
     */
    @Bean
    public Binding inventoryStringBinding() {
        return BindingBuilder
                .bind(inventoryStringQueue())
                .to(exchange())
                .with(inventoryRoutingKey);
    }

    /**
     * Defines a {@link Binding} bean that binds the JSON queue to the exchange using the routing key
     * specified by the {@code rabbitmq.inventory.routing.key.json.name} property.
     *
     * @return the configured {@code Binding} bean for the JSON queue
     */
    @Bean
    public Binding inventoryJSONBinding() {
        return BindingBuilder
                .bind(inventoryJSONQueue())
                .to(exchange())
                .with(inventoryJSONRoutingKey);
    }
}
