package org.citi.bean;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Carson
 * @created 2025/6/12 10:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(ESMapperScannerRegister.class)
@Repeatable(ESMapperScans.class)
public @interface ESMapperScan {
    String[] value() default {};
    String[] basePackages() default {};
    String dslPath() default "";
}
