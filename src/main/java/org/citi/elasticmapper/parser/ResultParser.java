package org.citi.elasticmapper.parser;

import org.citi.elasticmapper.metadata.MethodMetadata;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 04:59
 */
public interface ResultParser {
    Object parse(String body, MethodMetadata methodMetadata);
}
