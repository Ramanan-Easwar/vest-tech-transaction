package com.org.investechtransaction.config;

import com.org.investechtransaction.helper.RabbitMqListener;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.host}")
    String host;
    @Value("${spring.rabbitmq.username}")
    String username;
    @Value("${spring.rabbitmq.password}")
    String password;
    @Value("${rabbitmq.queue.transaction.buy}")
    String queueBuy;
    @Value("${rabbitmq.queue.transaction.sell}")
    String queueSell;
    @Value("${rabbitmq.queue.transaction.port}")
    Integer port;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(this.host);
        connectionFactory.setPort(this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(
            ConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter,
            Queue sellQueue, Queue buyQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(sellQueue, buyQueue);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(
            RabbitMqListener rabbitMqListener) {
        return new MessageListenerAdapter(rabbitMqListener);
    }

    @Bean
    public RabbitMqListener rabbitMqListener() {
        return new RabbitMqListener();
    }

    @Bean
    public Queue sellQueue() {
        return new Queue(this.queueSell);
    }

    @Bean
    public Queue buyQueue() {
        return new Queue(this.queueBuy);
    }


}
