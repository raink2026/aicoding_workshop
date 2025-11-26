package com.aibank.mcpdemo.service;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CozeWorkflowService {

    private static final Logger log = LoggerFactory.getLogger(CozeWorkflowService.class);

    /**
     * 常量（建议生产环境通过配置文件或环境变量注入）
     */
    private static final String API_URL = "https://api.coze.cn/v3/chat";
    // 注意：生产环境请不要把密钥写死在代码中，改用 Spring @Value 注入或从 Vault/环境变量读取
//    private static final String API_KEY = "cztei_hSj9rxZiZPAGT75zAlniKjtjPIGfo1HMSx0Nz8V7OgEmCr593VG1HiwGsEHj7TI55";
    private static final String BOT_ID = "7547602177159086089";
    private static final String DEFAULT_USER_ID = "123456789";
    private static final String DEFAULT_PROMPT = "hello";

    private static final String API_KEY = "pat_i6PP30orR6Rsv6ueMnF34Z7TRaPdsr0907g0ifyBEowMwP26NfWClU7Vyf8CIe4W";

    // 如果你希望从 application.properties 注入，使用类似下面注释的方式：
    // @Value("${coze.api.url}") private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CozeWorkflowService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 调用 Coze 聊天接口
     *
     * @param query 用户输入内容，若为 null 或空白则使用默认提示
     * @return Coze 返回的原始响应体（字符串），在出错时返回带错误信息的字符串
     */
    public String cozeChatAssistant(String query) {
        log.info("开始调用Coze聊天接口，用户输入: {}", query);
        String effectiveQuery = StrUtil.isBlank(query) ? DEFAULT_PROMPT : query;
        log.debug("有效查询内容: {}", effectiveQuery);

        try {
            // headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("bot_id", BOT_ID);
            requestBody.put("user_id", DEFAULT_USER_ID);
            requestBody.put("stream", true);

            Map<String, Object> message = new HashMap<>();
            message.put("content", effectiveQuery);
            message.put("content_type", "text");
            message.put("role", "user");
            message.put("type", "question");

            requestBody.put("additional_messages", Collections.singletonList(message));
            requestBody.put("parameters", Collections.emptyMap());

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.debug("请求体内容: {}", jsonBody);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            log.debug("请求头信息: Content-Type={}, Accept={}, Authorization=Bearer ***",
                    headers.getContentType(), headers.getAccept());

            log.info("发送POST请求到Coze API: {}", API_URL);
            ResponseEntity<String> response = restTemplate.postForEntity(URI.create(API_URL), requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Coze API调用成功，HTTP状态码: {}", response.getStatusCode());
                log.debug("Coze API响应体: {}", response.getBody());
                return response.getBody();
            } else {
                log.warn("Coze 返回非 2xx 状态: {}，body={}", response.getStatusCodeValue(), response.getBody());
                return String.format("调用Coze失败: HTTP %d", response.getStatusCodeValue());
            }
        } catch (JsonProcessingException jpe) {
            log.error("请求体序列化失败", jpe);
            return "请求序列化失败: " + jpe.getMessage();
        } catch (RestClientException rce) {
            log.error("调用 Coze 失败（HTTP 层面）", rce);
            return "调用Coze聊天时发生错误: " + rce.getMessage();
        } catch (Exception e) {
            log.error("调用 Coze 时发生未知错误", e);
            return "调用Coze聊天时发生错误: " + e.getMessage();
        }
    }
}
