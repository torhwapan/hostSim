package com.example.xmlparser.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Value("${mq.queue.name}")
    private String queueName;

    @Value("${mq.exchange.name}")
    private String exchangeName;

    @Value("${mq.routing.key}")
    private String routingKey;

    // 创建队列
    @Bean
    public Queue queue() {
        return new Queue(queueName, true); // durable=true
    }

    // 创建交换机
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    // 绑定队列和交换机
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

}