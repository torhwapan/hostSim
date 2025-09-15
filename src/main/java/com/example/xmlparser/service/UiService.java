package com.example.xmlparser.service;

import com.example.xmlparser.model.Message;
import com.example.xmlparser.model.MessageProperty;
import com.example.xmlparser.model.Property;
import com.example.xmlparser.model.Simulator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UiService {
    // 获取所有模拟器
    List<Simulator> getAllSimulators();
    
    // 获取消息记录
    List<Message> getMessages();
    
    // 获取指定消息详情
    Message getMessageDetail(String tid);
    
    // 获取属性信息
    Property getPropertyBySimulator(String simulatorName);
    
    // 上传XML文件并解析，关联到特定模拟器
    List<MessageProperty> uploadXmlFile(MultipartFile file, String simulatorName);
    
    // 保存消息属性配置，关联到特定模拟器
    void saveMessageProperties(List<MessageProperty> properties, String simulatorName);
    
    // 发送消息
    void sendMessage(String content);
    
    // 发送消息（带模拟器名称）
    void sendMessage(String content, String simulatorName);
    
    // 获取指定模拟器的消息属性
    List<MessageProperty> getMessagePropertiesBySimulator(String simulatorName);
}