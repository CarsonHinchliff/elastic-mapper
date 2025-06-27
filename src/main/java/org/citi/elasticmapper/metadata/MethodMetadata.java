package org.citi.elasticmapper.metadata;

import lombok.Data;
import org.citi.elasticmapper.parser.ResultParser;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 04:58
 */
@Data
public class MethodMetadata {
    public MethodMetadata(Method method) {
        this.method = method;
    }

    private String id;
    private Method method;
    private List<String> paramsName;
    private Type returnType;
    private Class<?> resturnClass;
    private String defaultIndex;
    private Integer idParamIndex;
    private ResultParser resultParser;
    private String urlParameters;
}
