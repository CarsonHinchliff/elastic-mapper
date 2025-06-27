package org.citi.elasticmapper.executor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.elasticmapper.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:32
 */
public class MapperPostExecutor implements MapperMethodExecutor{
    private final MethodMetadata methodMetadata;

    public MapperPostExecutor(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        index = index == null ? this.methodMetadata.getDefaultIndex() : index;
        Response response = this.defaultExecute(restClient, index, method, args);
        String responseBody = EntityUtils.toString(response.getEntity());
        return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
    }

    private Response defaultExecute(RestClient restClient, String index, Method method, Object[] args) throws IOException {
        Integer idParamIndex = this.methodMetadata.getIdParamIndex();
        Object document = null == idParamIndex ? args[0] : args[1 - idParamIndex];
        String endpoint = null == idParamIndex ? "/" + index + "/_doc" : "/" + index + "/_doc/" + args[idParamIndex];
        if (StringUtils.isNotBlank(this.methodMetadata.getUrlParameters())){
            endpoint = endpoint + this.methodMetadata.getUrlParameters();
        }

        Request request = new Request("POST", endpoint);
        request.setJsonEntity(JSON.toJSONString(document));
        return restClient.performRequest(request);
    }
}
