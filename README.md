# ChatBot - AI聊天机器人服务

## 项目简介

ChatBot是一个基于Spring Boot的AI聊天机器人服务，提供AI对话、用户资料管理和大五人格测试功能。用户可以通过API接口与AI进行对话，管理个人资料，并进行心理测试。

## 核心功能

- **AI聊天对话**：与AI机器人进行智能对话
- **用户资料管理**：创建、查看、更新用户个人资料
- **大五人格测试**：进行心理测试并获取分析报告

## 技术栈

- **后端框架**：Spring Boot 2.7.0
- **数据库**：MySQL + MyBatis Plus
- **AI服务**：Deepseek API
- **开发语言**：Java 1.8
- **构建工具**：Maven

## 快速开始

### 环境要求

- JDK 1.8+
- MySQL 5.7+
- Maven 3.6+

### 安装步骤

1. 克隆项目
```bash
git clone <your-repository-url>
cd ChatBot/KsanaChatBotService
```

2. 配置数据库
```sql
-- 创建数据库
CREATE DATABASE chatbot_db;

-- 导入数据表结构（参考chatbot-resources目录下的SQL文件）
```

3. 配置application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chatbot_db
    username: your_username
    password: your_password
    
# 配置Deepseek API密钥
deepseek:
  api-key: your_deepseek_api_key
```

4. 编译运行
```bash
mvn clean package
java -jar target/ksana-chatbot-service.jar
```

## API接口文档

### 1. AI聊天接口

#### 1.1 获取聊天联系人列表
- **接口地址**：`GET /api/chat/getContactsById`
- **请求头**：`Authorization: Bearer <token>`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "contactId": "ai_001",
      "contactName": "AI助手",
      "lastMessage": "你好，有什么可以帮助你的吗？",
      "lastMessageTime": "2024-01-01 12:00:00"
    }
  ]
}
```

#### 1.2 添加聊天联系人
- **接口地址**：`POST /api/chat/addContact`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "contactId": "ai_001"
}
```

#### 1.3 发送消息
- **接口地址**：`POST /api/chat/send`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "contactId": "ai_001",
  "message": "你好，AI助手！"
}
```
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "messageId": "msg_123",
    "aiResponse": "你好！我是AI助手，很高兴为你服务。有什么问题我可以帮助你解答吗？",
    "timestamp": "2024-01-01 12:00:00"
  }
}
```

#### 1.4 获取聊天历史
- **接口地址**：`GET /api/chat/history`
- **请求头**：`Authorization: Bearer <token>`
- **请求参数**：`contactId=ai_001`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "messageId": "msg_123",
      "senderId": "user_001",
      "receiverId": "ai_001",
      "message": "你好，AI助手！",
      "messageType": "USER",
      "sendTime": "2024-01-01 12:00:00"
    },
    {
      "messageId": "msg_124",
      "senderId": "ai_001",
      "receiverId": "user_001",
      "message": "你好！我是AI助手...",
      "messageType": "AI",
      "sendTime": "2024-01-01 12:00:01"
    }
  ]
}
```

#### 1.5 清空聊天历史
- **接口地址**：`POST /api/chat/clearHistory`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "contactId": "ai_001"
}
```

#### 1.6 删除聊天联系人
- **接口地址**：`POST /api/chat/deleteContact`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "contactId": "ai_001"
}
```

### 2. 用户资料接口

#### 2.1 获取用户资料
- **接口地址**：`GET /api/aiProfile/getUserProfile`
- **请求头**：`Authorization: Bearer <token>`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "user_001",
    "nickname": "张三",
    "avatar": "avatar_url",
    "description": "这是我的个人简介",
    "createTime": "2024-01-01 10:00:00"
  }
}
```

#### 2.2 保存用户资料
- **接口地址**：`POST /api/aiProfile/saveUserProfile`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "nickname": "张三",
  "description": "这是我的个人简介"
}
```

#### 2.3 上传头像
- **接口地址**：`POST /api/aiProfile/uploadAvatar`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：`multipart/form-data`，文件字段名为`file`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "avatarUrl": "http://example.com/avatar/user_001.jpg"
  }
}
```

#### 2.4 检查昵称是否可用
- **接口地址**：`POST /api/aiProfile/checkNickname`
- **请求体**：
```json
{
  "nickname": "张三"
}
```
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "available": true
  }
}
```

#### 2.5 更新昵称
- **接口地址**：`POST /api/aiProfile/updateNickname`
- **请求体**：
```json
{
  "nickname": "新昵称"
}
```

### 3. 大五人格测试接口

#### 3.1 提交测试结果
- **接口地址**：`POST /api/personalityTest/submit`
- **请求头**：`Authorization: Bearer <token>`
- **请求体**：
```json
{
  "personalityScores": {
    "openness": 75,
    "conscientiousness": 80,
    "extraversion": 65,
    "agreeableness": 70,
    "neuroticism": 45
  }
}
```

#### 3.2 获取最新测试结果
- **接口地址**：`GET /api/personalityTest/latest`
- **请求头**：`Authorization: Bearer <token>`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "test_001",
    "userId": "user_001",
    "openness": 75,
    "conscientiousness": 80,
    "extraversion": 65,
    "agreeableness": 70,
    "neuroticism": 45,
    "testTime": "2024-01-01 14:00:00"
  }
}
```

#### 3.3 获取所有用户测试结果（分页）
- **接口地址**：`GET /api/personalityTest/allUsers`
- **请求参数**：
  - `page`：页码（默认1）
  - `size`：每页大小（默认20）
  - `excludeIds`：排除的用户ID列表（可选）
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 20,
    "records": [
      {
        "userId": "user_001",
        "nickname": "张三",
        "openness": 75,
        "conscientiousness": 80,
        "extraversion": 65,
        "agreeableness": 70,
        "neuroticism": 45,
        "testTime": "2024-01-01 14:00:00"
      }
    ]
  }
}
```

#### 3.4 根据昵称搜索测试结果
- **接口地址**：`GET /api/personalityTest/searchByNickname`
- **请求参数**：`nickname=张三`
- **响应示例**：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": "user_001",
    "nickname": "张三",
    "openness": 75,
    "conscientiousness": 80,
    "extraversion": 65,
    "agreeableness": 70,
    "neuroticism": 45,
    "testTime": "2024-01-01 14:00:00"
  }
}
```

## 响应格式说明

所有API接口都使用统一的响应格式：

```json
{
  "code": 200,        // 状态码：200成功，其他为错误码
  "message": "success", // 响应消息
  "data": {}          // 响应数据，具体结构根据接口而定
}
```

### 常见状态码

- `200`：请求成功
- `400`：请求参数错误
- `401`：未授权访问
- `403`：权限不足
- `404`：资源不存在
- `500`：服务器内部错误

## 数据库表结构

### 1. ai_profile（用户资料表）
```sql
CREATE TABLE `ai_profile` (
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `description` text COMMENT '个人描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
);
```

### 2. big5_personality（大五人格测试表）
```sql
CREATE TABLE `big5_personality` (
  `id` varchar(50) NOT NULL COMMENT '测试ID',
  `user_id` varchar(50) NOT NULL COMMENT '用户ID',
  `openness` int DEFAULT NULL COMMENT '开放性得分',
  `conscientiousness` int DEFAULT NULL COMMENT '尽责性得分',
  `extraversion` int DEFAULT NULL COMMENT '外向性得分',
  `agreeableness` int DEFAULT NULL COMMENT '宜人性得分',
  `neuroticism` int DEFAULT NULL COMMENT '神经质得分',
  `test_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '测试时间',
  PRIMARY KEY (`id`)
);
```

### 3. chat_message（聊天消息表）
```sql
CREATE TABLE `chat_message` (
  `message_id` varchar(50) NOT NULL COMMENT '消息ID',
  `sender_id` varchar(50) NOT NULL COMMENT '发送者ID',
  `receiver_id` varchar(50) NOT NULL COMMENT '接收者ID',
  `message` text NOT NULL COMMENT '消息内容',
  `message_type` varchar(10) NOT NULL COMMENT '消息类型：USER/AI',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`message_id`)
);
```

## 开发说明

### 项目结构
```
src/main/java/com/chatbot/
├── controller/          # 控制器层
├── service/            # 服务层
├── dao/               # 数据访问层
├── entity/            # 实体类
├── model/             # 数据传输对象
├── util/              # 工具类
├── config/            # 配置类
└── constants/         # 常量定义
```

### 扩展开发

1. **添加新的AI模型**：在`DeepseekClient`类中添加新的API调用方法
2. **扩展用户资料字段**：修改`AIProfile`实体类和对应的数据库表
3. **添加新的测试类型**：参考`PersonalityTestController`实现新的测试接口

## 许可证

本项目采用MIT许可证，详情请参阅LICENSE文件。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 📖 小红书：时空码头
- 🎵 B站：时空码头  
- 🎬 抖音：时空码头
- 📺 YouTube：时空码头/KsanaDock

---

**注意**：使用本服务前请确保已正确配置数据库连接和AI API密钥。
