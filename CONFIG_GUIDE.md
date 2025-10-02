# 配置指南

本文档将指导您如何正确配置项目以确保正常运行。

## 配置文件说明

项目包含以下配置文件：
- `application.yml` - 主配置文件（已清理敏感信息）
- `application-prod.yml` - 生产环境配置文件（已清理敏感信息）
- `application-example.yml` - 配置示例文件

## 必需配置项

### 1. 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**配置说明：**
- `url`: 数据库连接地址，请替换 `your_database_name` 为实际数据库名
- `username`: 数据库用户名
- `password`: 数据库密码

### 2. JWT 配置

```yaml
jwt:
  secret: your-jwt-secret-key-should-be-very-long-and-secure-replace-this
  expiration: 86400 # 24小时，单位：秒
```

**配置说明：**
- `secret`: JWT 签名密钥，请使用至少32位的随机字符串
- `expiration`: Token 过期时间（秒）

### 3. Deepseek API 配置

```yaml
deepseek:
  api-key: your_deepseek_api_key
```

**配置说明：**
- `api-key`: Deepseek API 密钥，请从 [Deepseek 官网](https://platform.deepseek.com/) 获取

## 可选配置项

### 1. 微信小程序配置

```yaml
wechat:
  mp:
    app-id: your_wechat_app_id
    secret: your_wechat_secret
    token: your_wechat_token
  redirect-domain: http://your-domain.com
```

### 2. 抖音小程序配置

```yaml
douyin:
  mp:
    client-key: your_douyin_client_key
    client-secret: your_douyin_client_secret
  redirect-domain: https://your-domain.com
```

### 3. 阿里云服务配置

```yaml
aliyun:
  sms:
    access-key-id: your_aliyun_access_key_id
    access-key-secret: your_aliyun_access_key_secret
    sign-name: your_sms_sign_name
    template-code: your_sms_template_code
  oss:
    endpoint: your_oss_endpoint
    access-key-id: your_aliyun_access_key_id
    access-key-secret: your_aliyun_access_key_secret
    bucket-name: your_bucket_name
```

## 配置步骤

1. **复制示例配置文件**
   ```bash
   cp src/main/resources/application-example.yml src/main/resources/application-local.yml
   ```

2. **修改配置文件**
   编辑 `application-local.yml`，填入实际的配置值

3. **设置活动配置文件**
   在启动时指定配置文件：
   ```bash
   java -jar target/chatbot-web-server-1.0-SNAPSHOT.jar --spring.profiles.active=local
   ```

## 环境变量配置

您也可以通过环境变量来配置敏感信息：

```bash
export DB_PASSWORD=your_actual_password
export JWT_SECRET=your_actual_jwt_secret
export DEEPSEEK_API_KEY=your_actual_api_key
```

然后在配置文件中引用：

```yaml
spring:
  datasource:
    password: ${DB_PASSWORD}
jwt:
  secret: ${JWT_SECRET}
deepseek:
  api-key: ${DEEPSEEK_API_KEY}
```

## 安全建议

1. **不要将敏感信息提交到版本控制系统**
2. **使用强密码和复杂的密钥**
3. **定期更换API密钥**
4. **在生产环境中使用环境变量或密钥管理服务**
5. **限制数据库用户权限**

## 常见问题

### Q: 数据库连接失败
A: 请检查数据库地址、用户名、密码是否正确，确保数据库服务正在运行

### Q: JWT Token 验证失败
A: 请确保 JWT 密钥配置正确，且客户端和服务端使用相同的密钥

### Q: API 调用失败
A: 请检查 API 密钥是否有效，网络连接是否正常

如有其他问题，请查看日志文件或联系开发团队。