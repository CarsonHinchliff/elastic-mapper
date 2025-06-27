package org.citi.elasticmapper.bean;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Carson
 * @created 2025/6/22 星期日 下午 02:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({ESMapperScannerRegister.RepeatingRegistrar.class})
public @interface ESMapperScans {
    ESMapperScan[] value();
}
