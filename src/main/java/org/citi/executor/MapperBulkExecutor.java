package org.citi.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.citi.data.*;
import org.citi.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:32
 */
public class MapperBulkExecutor implements MapperMethodExecutor{
    private final MethodMetadata methodMetadata;

    public MapperBulkExecutor(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        if (args.length <= 0){
            return null;
        } else {
            StringBuilder requestBody = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof BulkItem){
                    requestBody.append(parseBulkItem((BulkItem) arg));
                } else {
                    if (!(arg instanceof Collection)){
                        throw new RuntimeException("UnSupported parameter type, must in [BulkItem.class, Collection.class]");
                    }

                    Collection collection = (Collection) arg;
                    collection.forEach(e -> {
                        requestBody.append(parseBulkItem((BulkItem) e));
                        requestBody.append("\n");
                    });
                }
            }

            HttpEntity entity = new NStringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            String endpoint = "/_bulk";
            if (StringUtils.isNotBlank(this.methodMetadata.getUrlParameters())){
                endpoint += this.methodMetadata.getUrlParameters();
            }

            Request request = new Request("POST", endpoint);
            request.setEntity(entity);
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
        }
    }

    private String parseBulkItem(BulkItem bulkItem){
        String request;
        if (BulkOperate.UPDATE.equals(bulkItem.getOperate())){
            UpdateHeaderWrapped updateHeaderWrapped = new UpdateHeaderWrapped(bulkItem.getHeader());
            String updateWrappedContent = JSON.toJSONString(updateHeaderWrapped);
            return updateWrappedContent + "\n" + JSON.toJSONString(bulkItem.getData(), new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue});
        } else if (BulkOperate.CREATE.equals(bulkItem.getOperate())){
            CreateHeaderWrapped createHeaderWrapped = new CreateHeaderWrapped(bulkItem.getHeader());
            String createWrappedContent = JSON.toJSONString(createHeaderWrapped);
            return createWrappedContent + "\n" + JSON.toJSONString(bulkItem.getData(), new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue});
        } else if (BulkOperate.DELETE.equals(bulkItem.getOperate())){
            DeleteHeaderWrapped deleteHeaderWrapped = new DeleteHeaderWrapped(bulkItem.getHeader());
            return JSON.toJSONString(deleteHeaderWrapped);
        } else {
            throw new RuntimeException("unknown operation type, [create, update, delete]?");
        }
    }
}
