package com.example.xmlparser.service.impl;

import com.example.xmlparser.service.MqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqServiceImpl implements MqService {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public MqServiceImpl(RabbitTemplate rabbitTemplate,
                        @Value("${mq.exchange.name}") String exchangeName,
                        @Value("${mq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }

    @Override
    public void sendXmlMessage(String xmlContent) {
        // 可以在这里添加XML消息的特定处理逻辑
        rabbitTemplate.convertAndSend(exchangeName, routingKey, xmlContent);
    }
}