package org.citi.elasticmapper.parser;

import com.alibaba.fastjson.JSON;
import org.citi.elasticmapper.metadata.MethodMetadata;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:30
 */
public class OriginalResultParser implements ResultParser{
    @Override
    public Object parse(String body, MethodMetadata methodMetadata) {
        return null == methodMetadata.getReturnType() ?
                JSON.parseObject(body, methodMetadata.getResturnClass())
                : JSON.parseObject(body, methodMetadata.getReturnType());
    }
}
