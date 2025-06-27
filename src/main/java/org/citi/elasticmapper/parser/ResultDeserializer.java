package org.citi.elasticmapper.parser;

import org.citi.elasticmapper.metadata.MethodMetadata;

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
