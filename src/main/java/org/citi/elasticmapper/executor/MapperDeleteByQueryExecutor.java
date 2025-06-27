package org.citi.elasticmapper.executor;

import com.jfinal.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.elasticmapper.metadata.MethodMetadata;
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
public class MapperDeleteByQueryExecutor implements MapperMethodExecutor{
    private final Template template;
    private final MethodMetadata methodMetadata;

    public MapperDeleteByQueryExecutor(Template template, MethodMetadata methodMetadata) {
        this.template = template;
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        index = index == null ? this.methodMetadata.getDefaultIndex() : index;

        Map<String, Object> params = new HashMap<String, Object>();
        for(int i=0; i<args.length; i++) {
            params.put(this.methodMetadata.getParamsName().get(i), args[i]);
        }

        String dsl = this.template.renderToString(params);
        String endpoint = "/" + index + "/_delete_by_query";
        if (StringUtils.isNotBlank(this.methodMetadata.getUrlParameters())){
            endpoint = endpoint + "?" + this.methodMetadata.getUrlParameters() + "&conflicts=proceed";
        } else {
            endpoint += "?conflicts=proceed";
        }

        Request request = new Request("POST", endpoint);
        request.setJsonEntity(dsl);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
    }
}
