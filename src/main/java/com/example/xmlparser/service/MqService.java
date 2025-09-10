package com.example.xmlparser.service;

public interface MqService {
    
    // 发送消息到MQ
    void sendMessage(String message);
    
    // 发送XML消息到MQ
    void sendXmlMessage(String xmlContent);
    
}