package com.example.xmlparser.service.impl;

import com.example.xmlparser.service.XmlParserService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

@Service
public class XmlParserServiceImpl implements XmlParserService {

    @Override
    public List<Map<String, Object>> parseXmlSecondLevel(String xmlContent) {
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
            
            // 获取根元素
            Element rootElement = document.getDocumentElement();
            
            // 获取第二层级元素
            NodeList nodeList = rootElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Map<String, Object> elementMap = new HashMap<>();
                    elementMap.put("name", element.getNodeName());
                    elementMap.put("text", element.getTextContent().trim());
                    
                    // 获取所有属性
                    Map<String, String> attributes = new HashMap<>();
                    NamedNodeMap attrMap = element.getAttributes();
                    for (int j = 0; j < attrMap.getLength(); j++) {
                        Node attr = attrMap.item(j);
                        attributes.put(attr.getNodeName(), attr.getNodeValue());
                    }
                    elementMap.put("attributes", attributes);
                    
                    // 保存原始节点，用于后续获取详细信息
                    elementMap.put("originalNode", element);
                    
                    result.add(elementMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> parseXmlFromFile(String filePath) {
        try {
            Resource resource = new ClassPathResource(filePath);
            File file = resource.getFile();
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
            }
            return parseXmlSecondLevel(contentBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Map<String, Object> getElementDetails(Map<String, Object> element) {
        Map<String, Object> details = new HashMap<>(element);
        
        try {
            // 如果有原始节点，获取更详细的信息
            if (element.containsKey("originalNode")) {
                Element originalElement = (Element) element.get("originalNode");
                
                // 获取所有子元素
                List<Map<String, Object>> children = new ArrayList<>();
                NodeList childNodes = originalElement.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) node;
                        Map<String, Object> childMap = new HashMap<>();
                        childMap.put("name", childElement.getNodeName());
                        childMap.put("text", childElement.getTextContent().trim());
                        
                        Map<String, String> attributes = new HashMap<>();
                        NamedNodeMap attrMap = childElement.getAttributes();
                        for (int j = 0; j < attrMap.getLength(); j++) {
                            Node attr = attrMap.item(j);
                            attributes.put(attr.getNodeName(), attr.getNodeValue());
                        }
                        childMap.put("attributes", attributes);
                        
                        children.add(childMap);
                    }
                }
                details.put("children", children);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return details;
    }
}