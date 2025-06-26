package org.citi.executor;

import com.jfinal.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.ESMapperDSLParser;
import org.citi.annotation.Custom;
import org.citi.infra.MapperRuntimeContext;
import org.citi.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:34
 */
public class CustomMapperExecutor implements MapperMethodExecutor{
    private final Custom custom;
    private final Template template;
    private final MethodMetadata methodMetadata;

    public CustomMapperExecutor(Custom custom, Template template, MethodMetadata methodMetadata) {
        this.custom = custom;
        this.template = template;
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        index = index == null ? this.methodMetadata.getDefaultIndex() : index;

        Map<String, Object> params = new HashMap<>();
        for(int i = 0; i < args.length; i++) {
            params.put(this.methodMetadata.getParamsName().get(i), args[i]);
        }

        if (StringUtils.isNotBlank(index)){
            index = ESMapperDSLParser.getInstance().getEngine().getTemplateByString(index).renderToString(params);
        }

        MapperRuntimeContext.setIndex(index);

        try {
            String url = "";
            if (StringUtils.isNotBlank(index)){
                url = "/" + index;
            }
            if (StringUtils.isNotBlank(this.custom.operate())){
                url = url + "/" + this.custom.operate();
            }

            Request request = new Request(this.custom.method(),  url);
            if (this.custom.body()){
                String dsl = this.template.renderToString(params);
                request.setJsonEntity(dsl);
            }

            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            MapperRuntimeContext.removeIndex();
        }
    }
}
