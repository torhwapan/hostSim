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
    
    // 上传XML文件并解析
    List<MessageProperty> uploadXmlFile(MultipartFile file);
    
    // 保存消息属性配置
    void saveMessageProperties(List<MessageProperty> properties);
    
    // 发送消息
    void sendMessage(String content);
    
    // 获取当前的消息属性
    List<MessageProperty> getCurrentMessageProperties();
}