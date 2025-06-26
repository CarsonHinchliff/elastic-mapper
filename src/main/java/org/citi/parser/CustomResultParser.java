package org.citi.parser;

import org.citi.ESMapperDSLParser;
import org.citi.metadata.MethodMetadata;
import org.springframework.context.ApplicationContext;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:27
 */
public class CustomResultParser implements ResultParser{
    private final String bean;
    private final Class<? extends ResultDeserializer> deserializer;

    public CustomResultParser(String bean, Class<? extends ResultDeserializer> deserializer) {
        this.bean = bean;
        this.deserializer = deserializer;
    }

    @Override
    public Object parse(String body, MethodMetadata methodMetadata) {
        ApplicationContext applicationContext = ESMapperDSLParser.getInstance().getApplicationContext();
        ResultDeserializer deserializer = applicationContext.getBean(this.bean, this.deserializer);
        return deserializer.parse(body, methodMetadata);
    }
}
