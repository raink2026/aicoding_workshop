package com.aibank.mcpdemo.mcp.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tool {
    String name() default "";
    String description() default "";
}
