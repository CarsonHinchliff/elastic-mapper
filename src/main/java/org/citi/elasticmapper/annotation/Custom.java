package org.citi.elasticmapper.annotation;

import java.lang.annotation.*;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Custom {
    String method();
    String value();
    String index();
    String operate() default "";
    boolean body() default false;
}
