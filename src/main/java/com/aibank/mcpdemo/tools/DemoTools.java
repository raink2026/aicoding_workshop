package com.aibank.mcpdemo.tools;

import com.aibank.mcpdemo.mcp.annotation.Tool;
import com.aibank.mcpdemo.mcp.annotation.ToolParam;
import com.aibank.mcpdemo.service.CozeWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DemoTools {

    private static final Logger logger = LoggerFactory.getLogger(DemoTools.class);

    @Autowired
    private CozeWorkflowService cozeWorkflowService;

    @Autowired
    private RestTemplate restTemplate;

    @Tool(description = "将输入文本首字母转换为大写")
    public String capitalize(
            @ToolParam(description = "要处理的文本") String text
    ) {
        logger.info("开始执行首字母大写转换，输入文本: {}", text);
        if (text == null || text.isEmpty()) {
            logger.warn("输入文本为空，直接返回原值");
            return text;
        }
        String[] parts = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : parts) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        String result = sb.toString().trim();
        logger.info("首字母大写转换完成，输出文本: {}", result);
        return result;
    }

    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";

    /**
     * 计算两个数字之间的加减乘除运算
     *
     * @param a        第一个数字
     * @param b        第二个数字
     * @param operator 运算符，例如 +, -, *, /
     * @return 运算结果
     * @throws IllegalArgumentException 如果运算符不支持，则抛出此异常
     */
    @Tool(description = "计算两个数字之间的加减乘除运算", name = "calculate")
    public double calculate(
            @ToolParam(description = "第一个数字") double a,
            @ToolParam(description = "第二个数字") double b,
            @ToolParam(description = "运算符，例如 +, -, *, /") String operator
    ) {
        logger.info("开始计算: {} {} {}", a, operator, b);

        if (!new HashSet<>(Arrays.asList(PLUS, MINUS, MULTIPLY, DIVIDE)).contains(operator)) {
            logger.warn("不支持的运算符: {}", operator);
            throw new IllegalArgumentException("不支持的运算符: " + operator);
        }

        if (DIVIDE.equals(operator) && b == 0) {
            logger.warn("除数不能为零");
            throw new IllegalArgumentException("除数不能为零");
        }

        double result;
        switch (operator) {
            case PLUS:
                result = a + b;
                break;
            case MINUS:
                result = a - b;
                break;
            case MULTIPLY:
                result = a * b;
                break;
            case DIVIDE:
                result = a / b;
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }

        logger.info("计算完成: {} {} {} = {}", a, operator, b, result);
        return result;
    }

    /**
     * Java 助手
     *
     * @param query 用户输入的问题或任务描述
     * @return Java 相关的解答
     */
    @Tool(description = "Java助手", name = "javaAssistant")
    public String javaAssistant(
            @ToolParam(description = "Java 相关问题或任务描述") String query
    ) {
        logger.info("Java助手收到问题: {}", query);
        String result = "这里是 Java 助手的解答: " + query;
        logger.info("Java助手返回解答: {}", result);
        return result;
    }

    /**
     * Go 助手
     *
     * @param query 用户输入的问题或任务描述
     * @return Go 相关的解答
     */
    @Tool(description = "GO助手", name = "goAssistant")
    public String goAssistant(
            @ToolParam(description = "Go 相关问题或任务描述") String query
    ) {
        logger.info("Go助手收到问题: {}", query);
        String result = "这里是 Go 助手的解答: " + query;
        logger.info("Go助手返回解答: {}", result);
        return result;
    }

    /**
     * 前端助手
     *
     * @param query 用户输入的问题或任务描述
     * @return 前端开发相关的解答
     */
    @Tool(description = "前端助手", name = "frontendAssistant")
    public String frontendAssistant(
            @ToolParam(description = "前端开发相关问题或任务描述") String query
    ) {
        logger.info("前端助手收到问题: {}", query);
        String result = "这里是前端助手的解答: " + query;
        logger.info("前端助手返回解答: {}", result);
        return result;
    }

    /**
     * Python 助手
     *
     * @param query 用户输入的问题或任务描述
     * @return Python 相关的解答
     */
    @Tool(description = "Python助手", name = "pythonAssistant")
    public String pythonAssistant(
            @ToolParam(description = "Python 相关问题或任务描述") String query
    ) {
        logger.info("Python助手收到问题: {}", query);
        String result = "这里是 Python 助手的解答: " + query;
        logger.info("Python助手返回解答: {}", result);
        return result;
    }

    /**
     * 删除字符串中的所有数字
     *
     * @param input 输入的字符串
     * @return 删除数字后的字符串
     */
    @Tool(description = "删除字符串中的所有数字", name = "removeNumbers")
    public String removeNumbers(
            @ToolParam(description = "需要处理的字符串") String input
    ) {
        logger.info("开始删除字符串中的数字，输入: {}", input);
        if (input == null) {
            logger.warn("输入字符串为null，返回空字符串");
            return "";
        }
        String result = input.replaceAll("\\d", "");
        logger.info("删除数字完成，输出: {}", result);
        return result;
    }

    /**
     * SQL 助手
     *
     * @param query 用户输入的问题或任务描述
     * @return SQL 相关的解答
     */
    @Tool(description = "SQL助手", name = "sqlAssistant")
    public String sqlAssistant(
            @ToolParam(description = "SQL 相关问题或任务描述") String query
    ) {
        logger.info("SQL助手收到问题: {}", query);

        String result = cozeWorkflowService.cozeChatAssistant(query);
        logger.info("SQL助手返回解答: {}", result);
        return result;
    }

    /**
     * 行内通用知识问答助手
     *
     * @param query 用户输入的问题
     * @return 通用知识问答结果
     */
    @Tool(description = "行内通用知识问答助手", name = "qaAssistant")
    public String qaAssistant(
            @ToolParam(description = "用户输入的问题") String query
    ) {
        logger.info("通用知识问答助手收到问题: {}", query);
        String result = "这里是通用知识问答助手的解答: " + query;
        logger.info("通用知识问答助手返回解答: {}", result);
        return result;
    }

    /**
     * 具体代码库参考助手
     *
     * @param query 用户输入的参考请求
     * @return 代码库相关的解答
     */
    @Tool(description = "具体代码库参考助手", name = "codeLibAssistant")
    public String codeLibAssistant(
            @ToolParam(description = "代码库参考问题或任务描述") String query
    ) {
        logger.info("代码库参考助手收到问题: {}", query);
        String result = "这里是具体代码库参考助手的解答: " + query;
        logger.info("代码库参考助手返回解答: {}", result);
        return result;
    }

    /**
     * Coze聊天机器人助手
     *
     * @param query 用户输入的问题
     * @return Coze聊天机器人的回答
     */
    @Tool(description = "Coze聊天机器人助手", name = "cozeChatAssistant")
    public String cozeChatAssistant(
            @ToolParam(description = "用户输入的问题") String query
    ) {
        logger.info("Coze聊天机器人助手收到问题: {}", query);
        try {
            // 准备请求URL
            String url = "https://api.coze.cn/v3/chat";

            // 准备请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("cztei_hSj9rxZiZPAGT75zAlniKjtjPIGfo1HMSx0Nz8V7OgEmCr593VG1HiwGsEHj7TI55");
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 准备请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("bot_id", "7551269485118898176");
            requestBody.put("user_id", "123456789");
            requestBody.put("stream", true);

            List<Map<String, Object>> additionalMessages = new ArrayList<>();
            Map<String, Object> message = new HashMap<>();
            message.put("content", query != null ? query : "hello");
            message.put("content_type", "text");
            message.put("role", "user");
            message.put("type", "question");
            additionalMessages.add(message);

            requestBody.put("additional_messages", additionalMessages);
            requestBody.put("parameters", new HashMap<>());

            // 创建请求实体
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            logger.debug("发送请求到Coze API: URL={}, Headers={}, Body={}", url, headers, requestBody);

            // 发送请求并获取响应
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            logger.debug("收到Coze API响应: Status={}, Body={}", response.getStatusCode(), response.getBody());

            // 返回响应结果
            String result = response.getBody();
            logger.info("Coze聊天机器人助手返回解答");
            return result;
        } catch (Exception e) {
            logger.error("调用Coze聊天机器人时发生错误", e);
            return "调用Coze聊天机器人时发生错误: " + e.getMessage();
        }
    }

}
