package com.example.xmlparser.service;

import java.util.List;
import java.util.Map;

public interface XmlParserService {
    
    // 解析XML字符串，返回第二层级元素
    List<Map<String, Object>> parseXmlSecondLevel(String xmlContent);
    
    // 从文件解析XML
    List<Map<String, Object>> parseXmlFromFile(String filePath);
    
    // 获取XML元素的详细信息
    Map<String, Object> getElementDetails(Map<String, Object> element);
    
}