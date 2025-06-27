package org.citi.elasticmapper.executor;

import com.jfinal.template.Template;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
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
 * @created 2025/6/21 星期六 上午 08:36
 */
public class MapperMGetExecutor implements MapperMethodExecutor{
    private final Template template;
    private final MethodMetadata methodMetadata;

    public MapperMGetExecutor(Template template, MethodMetadata methodMetadata) {
        this.template = template;
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {

        Map<String, Object> params = new HashMap<String, Object>();
        for(int i=0;i<args.length;i++){
            params.put(this.methodMetadata.getParamsName().get(i), args[i]);
        }

        String dsl = this.template.renderToString(params).trim() + "\r\n";

        Request request = new Request("GET", "_msearch");
        HttpEntity entity = new NStringEntity(dsl, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
    }
}
