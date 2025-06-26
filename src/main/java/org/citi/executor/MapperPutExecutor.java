package org.citi.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.data.DocWrapped;
import org.citi.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:33
 */
public class MapperPutExecutor implements MapperMethodExecutor{
    private final MethodMetadata methodMetadata;

    public MapperPutExecutor(MethodMetadata methodMetadata) {
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
        Object id = args[this.methodMetadata.getIdParamIndex()];
        Object data = args[1 - this.methodMetadata.getIdParamIndex()];
        DocWrapped docWrapped = new DocWrapped(data);
        String endpoint = "/" + index + "/_update" + id;
        if (StringUtils.isNotBlank(this.methodMetadata.getUrlParameters())){
            endpoint += this.methodMetadata.getUrlParameters();
        }
        Request request = new Request("POST", endpoint);
        request.setJsonEntity(JSON.toJSONString(docWrapped, new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect} ));
        return restClient.performRequest(request);
    }
}
