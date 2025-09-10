package com.example.xmlparser.controller;

import com.example.xmlparser.service.MqService;
import com.example.xmlparser.service.XmlParserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class XmlParserController {

    private final XmlParserService xmlParserService;
    private final MqService mqService;
    private final String demoFilePath;

    public XmlParserController(XmlParserService xmlParserService,
                              MqService mqService,
                              @Value("${xml.demo.file.path}") String demoFilePath) {
        this.xmlParserService = xmlParserService;
        this.mqService = mqService;
        this.demoFilePath = demoFilePath;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 解析演示XML文件并展示
        List<Map<String, Object>> elements = xmlParserService.parseXmlFromFile(demoFilePath);
        model.addAttribute("elements", elements);
        return "index";
    }

    @PostMapping("/parse")
    public String parseXml(@RequestParam("xmlContent") String xmlContent, Model model) {
        List<Map<String, Object>> elements = xmlParserService.parseXmlSecondLevel(xmlContent);
        model.addAttribute("elements", elements);
        model.addAttribute("xmlContent", xmlContent);
        return "index";
    }

    @PostMapping("/getDetails")
    public String getElementDetails(@RequestParam("elementIndex") int elementIndex,
                                   @RequestParam("xmlContent") String xmlContent,
                                   Model model) {
        List<Map<String, Object>> elements = xmlParserService.parseXmlSecondLevel(xmlContent);
        model.addAttribute("elements", elements);
        model.addAttribute("xmlContent", xmlContent);
        
        if (elementIndex >= 0 && elementIndex < elements.size()) {
            Map<String, Object> details = xmlParserService.getElementDetails(elements.get(elementIndex));
            model.addAttribute("selectedElement", details);
        }
        
        return "index";
    }

    @PostMapping("/sendToMq")
    public String sendToMq(@RequestParam("xmlContent") String xmlContent,
                          @RequestParam("selectedElements") List<String> selectedElements,
                          Model model) {
        List<Map<String, Object>> elements = xmlParserService.parseXmlSecondLevel(xmlContent);
        model.addAttribute("elements", elements);
        model.addAttribute("xmlContent", xmlContent);
        
        // 可以根据selectedElements做相应处理，然后发送到MQ
        mqService.sendXmlMessage(xmlContent);
        model.addAttribute("message", "消息已发送到MQ");
        
        return "index";
    }

    @PostMapping("/updateConfig")
    public String updateConfig(@RequestParam Map<String, String> configParams, Model model) {
        // 这里可以处理系统连接配置的更新
        // 实际应用中可能需要持久化配置
        model.addAttribute("configMessage", "配置已更新");
        
        // 重新加载XML元素
        List<Map<String, Object>> elements = xmlParserService.parseXmlFromFile(demoFilePath);
        model.addAttribute("elements", elements);
        
        return "index";
    }

    @PostMapping("/updateMqConfig")
    public String updateMqConfig(@RequestParam Map<String, String> mqConfigParams, Model model) {
        // 这里可以处理MQ连接配置的更新
        // 实际应用中可能需要动态更新MQ连接
        model.addAttribute("mqConfigMessage", "MQ配置已更新");
        
        // 重新加载XML元素
        List<Map<String, Object>> elements = xmlParserService.parseXmlFromFile(demoFilePath);
        model.addAttribute("elements", elements);
        
        return "index";
    }
}