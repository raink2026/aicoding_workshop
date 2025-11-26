package com.aibank.mcpdemo.mcp;

import com.aibank.mcpdemo.mcp.annotation.Tool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ToolRegistry implements ApplicationListener<ContextRefreshedEvent> {

    private final Map<String, Method> toolMap = new ConcurrentHashMap<>();
    private final Map<String, Object> toolBeanMap = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        String[] beanNames = context.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Tool.class)) {
                    Tool tool = method.getAnnotation(Tool.class);
                    String toolName = tool.name().isEmpty() ? method.getName() : tool.name();
                    toolMap.put(toolName, method);
                    toolBeanMap.put(toolName, bean);
                }
            }
        }
    }

    public Method getTool(String name) {
        return toolMap.get(name);
    }

    public Object getToolBean(String name) {
        return toolBeanMap.get(name);
    }

    public Map<String, Method> getAllTools() {
        return new HashMap<>(toolMap);
    }
}
