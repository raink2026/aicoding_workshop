# mcp-demo-java

Spring Boot + MCP demo project (package `com.aibank.mcpdemo`)

## 项目结构

- `pom.xml` — Maven 构建文件（Spring Boot + Java17）
- `src/main/java/com/aibank/mcpdemo/`：
    - `McpDemoApplication.java` — 程序入口
    - `config/` — 配置类目录
        - `McpConfig.java` — MCP相关配置
        - `RestTemplateConfig.java` — RestTemplate配置
    - `service/` — 服务类目录
        - `CozeWorkflowService.java` — Coze工作流服务实现 demo
    - `tools/` — 工具类目录
        - `DemoTools.java` — 示例工具实现 demo
- `src/main/resources/`：
    - `application.yml` — 应用配置文件

## 功能说明

这个项目实现了基于Spring Boot的MCP（Model Context Protocol）服务端，提供了以下功能：

- **REST mode**: 运行在Spring Boot HTTP服务上（默认端口8088）
- **Streamable HTTP mode**: 支持Streamable HTTP连接

## 构建项目

```bash
mvn clean package -DskipTests
```

## 运行项目

运行（默认端口8088）：
```bash
java -jar target/mcp-demo-1.0.0.jar
```

项目启动后会在控制台输出日志，并显示MCP服务的相关信息。

## API端点

- Streamable HTTP端点: `http://localhost:8088/api/streaming`
- Streamable HTTP消息端点: `http://localhost:8088/api/streaming`

## Postman测试集合

项目根目录下的 `quickstart\mcp-demo-postman-collection.json` 包含了用于测试的请求集合。