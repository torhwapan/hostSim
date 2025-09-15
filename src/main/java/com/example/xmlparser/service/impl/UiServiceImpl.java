package com.example.xmlparser.service.impl;

import com.example.xmlparser.model.Message;
import com.example.xmlparser.model.MessageProperty;
import com.example.xmlparser.model.Property;
import com.example.xmlparser.model.Simulator;
import com.example.xmlparser.service.UiService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UiServiceImpl implements UiService {
    // 使用线程安全的集合存储数据
    private final List<Simulator> simulators = new CopyOnWriteArrayList<>();
    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final Map<String, Property> propertiesMap = new ConcurrentHashMap<>();
    // 按模拟器名称存储消息属性，实现持久化
    private final Map<String, List<MessageProperty>> simulatorMessageProperties = new ConcurrentHashMap<>();
    
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
        
        // 为每个模拟器初始化不同的默认消息属性数据
        // Simulator1的消息属性
        List<MessageProperty> properties1 = new ArrayList<>();
        properties1.add(new MessageProperty("OrderRequest_S1", "Primary trading order request message", true));
        properties1.add(new MessageProperty("MarketData_S1", "Primary market data message", false));
        properties1.add(new MessageProperty("ExecutionReport_S1", "Primary execution report message", false));
        simulatorMessageProperties.put("Simulator1", properties1);
        
        // Simulator2的消息属性
        List<MessageProperty> properties2 = new ArrayList<>();
        properties2.add(new MessageProperty("OrderRequest_S2", "Backup trading order request message", true));
        properties2.add(new MessageProperty("MarketData_S2", "Backup market data message", false));
        properties2.add(new MessageProperty("BackupStatus_S2", "Backup system status message", false));
        simulatorMessageProperties.put("Simulator2", properties2);
        
        // Simulator3的消息属性
        List<MessageProperty> properties3 = new ArrayList<>();
        properties3.add(new MessageProperty("MarketData_S3", "Detailed market data message", true));
        properties3.add(new MessageProperty("PriceUpdate_S3", "Real-time price update message", false));
        properties3.add(new MessageProperty("MarketStatus_S3", "Market status notification message", false));
        simulatorMessageProperties.put("Simulator3", properties3);
        
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
    public List<MessageProperty> uploadXmlFile(MultipartFile file, String simulatorName) {
        try {
            // 这里只是模拟文件上传和解析
            // 实际应用中应该解析XML文件内容
            String fileName = file.getOriginalFilename();
            
            // 添加一条上传记录到消息列表
            messages.add(new Message("File uploaded", "Successfully uploaded file: " + fileName + " for simulator: " + simulatorName));
            
            // 返回模拟的解析结果
            List<MessageProperty> newProperties = new ArrayList<>();
            newProperties.add(new MessageProperty(fileName.replace(".xml", ""), 
                    "Imported from uploaded file", false));
            newProperties.add(new MessageProperty("UploadedConfig", 
                    "Configuration imported from " + fileName, false));
            
            // 为指定模拟器存储解析结果，支持修改替换
            simulatorMessageProperties.put(simulatorName, newProperties);
            
            return new ArrayList<>(newProperties);
        } catch (Exception e) {
            // 记录错误消息
            messages.add(new Message("File upload failed", "Error uploading file: " + e.getMessage()));
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public void saveMessageProperties(List<MessageProperty> properties, String simulatorName) {
        // 为指定模拟器保存消息属性配置
        simulatorMessageProperties.put(simulatorName, new ArrayList<>(properties));
        
        // 确保只有一个默认属性
        long defaultCount = properties.stream().filter(MessageProperty::isDefault).count();
        if (defaultCount > 1) {
            // 如果有多个默认属性，只保留第一个
            boolean firstDefault = false;
            List<MessageProperty> updatedProperties = simulatorMessageProperties.get(simulatorName);
            if (updatedProperties != null) {
                for (MessageProperty prop : updatedProperties) {
                    if (prop.isDefault()) {
                        if (!firstDefault) {
                            firstDefault = true;
                        } else {
                            prop.setDefault(false);
                        }
                    }
                }
            }
        }
        
        // 添加保存记录到消息列表
        messages.add(new Message("Properties saved", "Message properties configuration saved successfully for simulator: " + simulatorName));
    }
    
    @Override
    public void sendMessage(String content) {
        // 添加发送的消息到消息列表
        messages.add(new Message("Message sent", "Content: " + content));
    }
    
    @Override
    public void sendMessage(String content, String simulatorName) {
        // 添加发送的消息到消息列表，并包含模拟器名称
        messages.add(new Message("Message sent from " + simulatorName, "Content: " + content));
    }
    
    @Override
    public List<MessageProperty> getMessagePropertiesBySimulator(String simulatorName) {
        // 获取指定模拟器的消息属性，如果没有则返回空列表
        return simulatorMessageProperties.getOrDefault(simulatorName, new ArrayList<>());
    }
}