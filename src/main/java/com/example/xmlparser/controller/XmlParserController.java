package com.example.xmlparser.controller;

import com.example.xmlparser.model.Message;
import com.example.xmlparser.model.MessageProperty;
import com.example.xmlparser.model.Property;
import com.example.xmlparser.model.Simulator;
import com.example.xmlparser.service.MqService;
import com.example.xmlparser.service.UiService;
import com.example.xmlparser.service.XmlParserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class XmlParserController {

    private final XmlParserService xmlParserService;
    private final MqService mqService;
    private final UiService uiService;
    private final String demoFilePath;

    public XmlParserController(XmlParserService xmlParserService,
                              MqService mqService,
                              UiService uiService,
                              @Value("${xml.demo.file.path}") String demoFilePath) {
        this.xmlParserService = xmlParserService;
        this.mqService = mqService;
        this.uiService = uiService;
        this.demoFilePath = demoFilePath;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 添加所有需要的数据到模型
        model.addAttribute("simulators", uiService.getAllSimulators());
        model.addAttribute("messages", uiService.getMessages());
        
        // 默认选择第一个模拟器的属性
        List<Simulator> simulators = uiService.getAllSimulators();
        if (!simulators.isEmpty()) {
            model.addAttribute("selectedProperty", uiService.getPropertyBySimulator(simulators.get(0).getName()));
            // 添加选中模拟器的消息属性
            model.addAttribute("messageProperties", uiService.getMessagePropertiesBySimulator(simulators.get(0).getName()));
        }
        
        return "index";
    }

    // 1. 获取模拟器列表
    @GetMapping("/api/simulators")
    @ResponseBody
    public List<Simulator> getSimulators() {
        return uiService.getAllSimulators();
    }
    
    // 2. 获取消息记录
    @GetMapping("/api/messages")
    @ResponseBody
    public List<Message> getMessages() {
        return uiService.getMessages();
    }
    
    // 3. 获取消息详情
    @GetMapping("/api/message/detail/{tid}")
    @ResponseBody
    public Message getMessageDetail(@PathVariable String tid) {
        return uiService.getMessageDetail(tid);
    }
    
    // 4. 获取属性信息
    @GetMapping("/api/property/{simulatorName}")
    @ResponseBody
    public Property getProperty(@PathVariable String simulatorName) {
        return uiService.getPropertyBySimulator(simulatorName);
    }
    
    // 5. 获取指定模拟器的消息属性
    @GetMapping("/api/message-properties/{simulatorName}")
    @ResponseBody
    public List<MessageProperty> getMessagePropertiesBySimulator(@PathVariable String simulatorName) {
        return uiService.getMessagePropertiesBySimulator(simulatorName);
    }
    
    // 6. 上传XML文件并关联到特定模拟器
    @PostMapping("/api/upload")
    @ResponseBody
    public List<MessageProperty> uploadFile(@RequestParam("file") MultipartFile file, 
                                           @RequestParam("simulatorName") String simulatorName) {
        return uiService.uploadXmlFile(file, simulatorName);
    }
    
    // 7. 保存消息属性配置，关联到特定模拟器
    @PostMapping("/api/save/properties")
    @ResponseBody
    public Map<String, String> saveProperties(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<MessageProperty> properties = (List<MessageProperty>) payload.get("properties");
        String simulatorName = (String) payload.get("simulatorName");
        
        uiService.saveMessageProperties(properties, simulatorName);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Properties saved successfully");
        return response;
    }
    
    // 8. 发送消息
    @PostMapping("/api/send/message")
    @ResponseBody
    public Map<String, String> sendMessage(@RequestBody Map<String, String> payload) {
        uiService.sendMessage(payload.get("content"));
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Message sent successfully");
        return response;
    }
}