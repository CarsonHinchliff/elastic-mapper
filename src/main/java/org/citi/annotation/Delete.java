package org.citi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Delete {
    String value() default "";
    String urlParameters() default "";
}
