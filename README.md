# XML解析器应用

这是一个基于Spring Boot的XML解析器应用，支持XML文件解析、MQ消息发送和详细的XML元素查看功能。

## 功能特性

1. **XML解析**：解析XML文件或输入的XML字符串，展示第二层级的元素信息。
2. **MQ配置**：支持RabbitMQ的连接配置和消息发送功能。
3. **元素详情**：查看XML元素的详细信息，包括属性和子元素。
4. **响应式布局**：页面布局清晰，分为系统连接配置、MQ连接配置、XML消息展示和XML详细消息体四个区域。

## 项目结构

```
XmlParserApp/
├── src/main/java/com/example/xmlparser/   # Java源代码
│   ├── controller/                       # 控制器
│   ├── service/                          # 服务接口
│   │   └── impl/                         # 服务实现
│   ├── config/                           # 配置类
│   └── XmlParserApplication.java         # 应用程序入口
├── src/main/resources/                   # 资源文件
│   ├── templates/                        # Thymeleaf模板
│   ├── static/                           # 静态资源
│   ├── application.properties            # 应用程序配置
│   └── demo.xml                          # 示例XML文件
├── pom.xml                               # Maven依赖配置
├── start.bat                             # Windows启动脚本
└── README.md                             # 项目说明文档
```

## 技术栈

- Java 8+
- Spring Boot 2.7.x
- Thymeleaf
- RabbitMQ
- JAXB/SAX (XML解析)

## 快速开始

### 前提条件

- JDK 8或更高版本
- Maven 3.6或更高版本
- RabbitMQ服务器（可选，如果需要使用MQ功能）

### 运行方法

1. **使用启动脚本**
   
   在Windows系统上，直接双击`start.bat`文件即可启动应用。

2. **手动运行**
   
   ```bash
   # 构建项目
   mvn clean install
   
   # 运行应用
   mvn spring-boot:run
   ```

3. **访问应用**
   
   应用启动后，在浏览器中访问 `http://localhost:8080` 即可使用。

## 使用说明

1. **XML解析**
   - 在XML内容输入框中输入XML字符串，或使用默认的示例XML
   - 点击"解析XML"按钮，系统将解析XML并在下方表格中显示第二层级的元素
   - 可以勾选元素，然后点击"发送选中到MQ"按钮将选中的元素发送到MQ

2. **查看元素详情**
   - 在XML消息展示区域，点击元素对应的"查看详情"按钮
   - 右侧的XML详细消息体区域将显示该元素的详细信息，包括属性和子元素

3. **配置设置**
   - 在系统连接配置区域可以设置系统URL、用户名和密码
   - 在MQ连接配置区域可以设置RabbitMQ的连接信息
   - 点击"保存配置"或"保存MQ配置"按钮保存设置

## 注意事项

- 如果需要使用MQ功能，请确保已安装并启动RabbitMQ服务器
- 默认的MQ配置是连接到本地的RabbitMQ服务器，用户名和密码均为"guest"
- 如果不使用MQ功能，仍然可以正常使用XML解析和元素查看功能

## 许可证

MIT



需求：
左上角：Simulators
表格内容：
Name, Connetcion,Description
SytemID    MQ             init success

右上角： Messages（展示消息推送记录）
Time  TID  Message

右中间：Message Detail
右上角选择具体消息时，展示消息详情


左中间: Properties 
Name   SytemID
Connection Type  MQ
Library:  (导入)
Connection Set(可展开)：
   IP
   Port
   Listen Queue
   Target Queue

左下：Message properties
Name  Description IsDefault
勾选完成后，可以保存》

右下： 一个send按钮，然后按钮下方一个文本框，根据左侧选择的 IsDefault展示具体文本内容。

那就是前后端总共4个接口
1，解析xml文件并回传接口
2，IsDefault选择后 保存接口
3，主动发送消息Send接口
4，后端记录的操作历史展示接口




