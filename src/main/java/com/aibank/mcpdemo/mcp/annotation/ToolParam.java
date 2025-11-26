package com.aibank.mcpdemo.mcp.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ToolParam {
    String name() default "";
    String description() default "";
}
