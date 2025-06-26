package org.citi.parser;

import org.citi.metadata.MethodMetadata;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:12
 */
public interface ResultDeserializer <T> {
    default T parse(String body, MethodMetadata methodMetadata){
        return this.parse(body);
    }

    T parse(String body);
}
