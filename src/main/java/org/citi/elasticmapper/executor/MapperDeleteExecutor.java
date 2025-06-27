package org.citi.elasticmapper.executor;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.elasticmapper.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.lang.reflect.Method;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:33
 */
public class MapperDeleteExecutor implements MapperMethodExecutor {
    private final MethodMetadata methodMetadata;

    public MapperDeleteExecutor(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        index = index == null ? this.methodMetadata.getDefaultIndex() : index;
        Object id = args[this.methodMetadata.getIdParamIndex()];
        String endpoint = "/" + index + "/_doc/" + id;
        if (StringUtils.isNotBlank(this.methodMetadata.getUrlParameters())){
            endpoint = endpoint + this.methodMetadata.getUrlParameters();
        }

        Request request = new Request("DELETE", endpoint);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
    }
}
