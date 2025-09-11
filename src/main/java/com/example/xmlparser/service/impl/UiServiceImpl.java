package com.example.xmlparser.service.impl;

import com.example.xmlparser.model.Message;
import com.example.xmlparser.model.MessageProperty;
import com.example.xmlparser.model.Property;
import com.example.xmlparser.model.Simulator;
import com.example.xmlparser.service.UiService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UiServiceImpl implements UiService {
    // 使用线程安全的集合存储数据
    private final List<Simulator> simulators = new CopyOnWriteArrayList<>();
    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final Map<String, Property> propertiesMap = new ConcurrentHashMap<>();
    private final List<MessageProperty> messageProperties = new CopyOnWriteArrayList<>();
    
    // 初始化模拟数据
    @PostConstruct
    public void init() {
        // 初始化模拟器数据
        simulators.add(new Simulator("Simulator1", "Active", "Primary trading simulator"));
        simulators.add(new Simulator("Simulator2", "Inactive", "Backup trading simulator"));
        simulators.add(new Simulator("Simulator3", "Maintenance", "Market data simulator"));
        
        // 初始化属性数据
        Property property1 = new Property("Simulator1", "SYS001", "TCP/IP", "RabbitMQ");
        property1.getConnectionSet().setIp("192.168.1.101");
        property1.getConnectionSet().setPort("5672");
        property1.getConnectionSet().setListenQueue("input.queue");
        property1.getConnectionSet().setTargetQueue("output.queue");
        propertiesMap.put("Simulator1", property1);
        
        Property property2 = new Property("Simulator2", "SYS002", "TCP/IP", "RabbitMQ");
        property2.getConnectionSet().setIp("192.168.1.102");
        property2.getConnectionSet().setPort("5672");
        property2.getConnectionSet().setListenQueue("input.queue2");
        property2.getConnectionSet().setTargetQueue("output.queue2");
        propertiesMap.put("Simulator2", property2);
        
        Property property3 = new Property("Simulator3", "SYS003", "TCP/IP", "RabbitMQ");
        property3.getConnectionSet().setIp("192.168.1.103");
        property3.getConnectionSet().setPort("5672");
        property3.getConnectionSet().setListenQueue("market.queue");
        property3.getConnectionSet().setTargetQueue("process.queue");
        propertiesMap.put("Simulator3", property3);
        
        // 初始化消息属性数据
        messageProperties.add(new MessageProperty("OrderRequest", "Trading order request message", true));
        messageProperties.add(new MessageProperty("MarketData", "Real-time market data message", false));
        messageProperties.add(new MessageProperty("ExecutionReport", "Order execution report message", false));
        
        // 初始化消息记录
        messages.add(new Message("System startup completed", "All services initialized successfully"));
        messages.add(new Message("Connection established", "Connected to market data feed"));
        messages.add(new Message("Configuration updated", "Trading parameters modified"));
    }
    
    @Override
    public List<Simulator> getAllSimulators() {
        return new ArrayList<>(simulators);
    }
    
    @Override
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }
    
    @Override
    public Message getMessageDetail(String tid) {
        return messages.stream()
                .filter(message -> message.getTid().equals(tid))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Property getPropertyBySimulator(String simulatorName) {
        return propertiesMap.get(simulatorName);
    }
    
    @Override
    public List<MessageProperty> uploadXmlFile(MultipartFile file) {
        try {
            // 这里只是模拟文件上传和解析
            // 实际应用中应该解析XML文件内容
            String fileName = file.getOriginalFilename();
            
            // 添加一条上传记录到消息列表
            messages.add(new Message("File uploaded", "Successfully uploaded file: " + fileName));
            
            // 返回模拟的解析结果
            List<MessageProperty> newProperties = new ArrayList<>();
            newProperties.add(new MessageProperty(fileName.replace(".xml", ""), 
                    "Imported from uploaded file", false));
            newProperties.add(new MessageProperty("UploadedConfig", 
                    "Configuration imported from " + fileName, false));
            
            // 添加到现有属性列表
            messageProperties.addAll(newProperties);
            
            return new ArrayList<>(messageProperties);
        } catch (Exception e) {
            // 记录错误消息
            messages.add(new Message("File upload failed", "Error uploading file: " + e.getMessage()));
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public void saveMessageProperties(List<MessageProperty> properties) {
        // 清空现有属性并更新
        messageProperties.clear();
        messageProperties.addAll(properties);
        
        // 确保只有一个默认属性
        long defaultCount = properties.stream().filter(MessageProperty::isDefault).count();
        if (defaultCount > 1) {
            // 如果有多个默认属性，只保留第一个
            boolean firstDefault = false;
            for (MessageProperty prop : messageProperties) {
                if (prop.isDefault()) {
                    if (!firstDefault) {
                        firstDefault = true;
                    } else {
                        prop.setDefault(false);
                    }
                }
            }
        }
        
        // 添加保存记录到消息列表
        messages.add(new Message("Properties saved", "Message properties configuration saved successfully"));
    }
    
    @Override
    public void sendMessage(String content) {
        // 添加发送的消息到消息列表
        messages.add(new Message("Message sent", "Content: " + content));
    }
    
    @Override
    public List<MessageProperty> getCurrentMessageProperties() {
        return new ArrayList<>(messageProperties);
    }
}