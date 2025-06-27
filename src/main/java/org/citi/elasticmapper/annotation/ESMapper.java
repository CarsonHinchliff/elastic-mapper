package org.citi.elasticmapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ESMapper {
    String defaultIndex() default "";
    String namespace() default "";
}
