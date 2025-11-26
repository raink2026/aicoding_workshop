package com.aibank.mcpdemo.mcp;

import com.aibank.mcpdemo.mcp.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mcp")
public class McpController {

    @Autowired
    private ToolRegistry toolRegistry;

    @GetMapping("/tools/list")
    public List<Map<String, Object>> listTools() {
        return toolRegistry.getAllTools().entrySet().stream()
                .map(entry -> {
                    Map<String, Object> toolInfo = new HashMap<>();
                    toolInfo.put("name", entry.getKey());
                    Tool tool = entry.getValue().getAnnotation(Tool.class);
                    toolInfo.put("description", tool.description());
                    return toolInfo;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/tools/call")
    public Map<String, Object> callTool(@RequestBody Map<String, Object> body) {
        Map<String, Object> params = (Map<String, Object>) body.get("params");
        String toolName = (String) params.get("name");
        Map<String, Object> args = (Map<String, Object>) params.get("arguments");

        Method method = toolRegistry.getTool(toolName);
        Object bean = toolRegistry.getToolBean(toolName);

        Object result = null;
        try {
            Parameter[] parameters = method.getParameters();
            Object[] methodArgs = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                String paramName = parameters[i].getName();
                Object argValue = args.get(paramName);
                if (parameters[i].getType().equals(double.class) || parameters[i].getType().equals(Double.class)) {
                    methodArgs[i] = Double.parseDouble(argValue.toString());
                } else {
                    methodArgs[i] = argValue;
                }
            }
            result = method.invoke(bean, methodArgs);
        } catch (Exception e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("jsonrpc", "2.0");
        response.put("id", body.get("id"));

        Map<String, Object> resultWrap = new HashMap<>();
        resultWrap.put("content", Arrays.asList(
                Collections.singletonMap("type", "text"),
                Collections.singletonMap("text", String.valueOf(result))
        ));
        response.put("result", resultWrap);

        return response;
    }
}
