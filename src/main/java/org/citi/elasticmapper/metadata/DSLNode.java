package org.citi.elasticmapper.metadata;

import com.jfinal.template.Template;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:38
 */
@Data
@AllArgsConstructor
public class DSLNode {
    private String id;
    private Template template;
}
