package org.citi.elasticmapper.metadata;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:00
 */
@Data
public class MapperMetadata {
    private Class<?> clazz;
    private Map<Method, MethodMetadata> methodMetadataMap;

    public MapperMetadata(Class<?> clazz) {
        this.clazz = clazz;
        this.methodMetadataMap = new HashMap<>();
    }

    public void addMethodMetadata(Method method, MethodMetadata methodMetadata) {
        this.methodMetadataMap.put(method, methodMetadata);
    }
}
