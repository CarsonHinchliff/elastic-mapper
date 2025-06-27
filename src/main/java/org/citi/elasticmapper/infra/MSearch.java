package org.citi.elasticmapper.infra;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.citi.elasticmapper.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class MSearch {
    private static final ThreadLocal<MSearchContext> CONTEXT = new ThreadLocal<>();

    public static void init(String name){
        MSearchContext context = new MSearchContext(name);
        CONTEXT.set(context);
    }


    @SuppressWarnings("unchecked")
    public static <T> Future<T> add(Supplier<T> supplier){
        MSearchContext context = (CONTEXT.get() != null) ? CONTEXT.get() : null;
        if (context == null){ throw new RuntimeException("No context found"); }

        supplier.get();
        int size = context.getFutures().size();
        return context.getFutures().get(size - 1);
    }

    public static void commit(){
        try {
            MSearchContext context = getContext();
            String dsl = context.getMSearchStatement().toString();
            log.info("dsl:\r\n: {}", dsl);
            Request request = new Request("GET", "_msearch");
            HttpEntity entity = new NStringEntity(dsl, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            long start = System.currentTimeMillis();
            Response response = context.getRestClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject responseJson = JSONObject.parseObject(responseBody);
            JSONArray responses = responseJson.getJSONArray("responses");
            long end = System.currentTimeMillis();
            log.info("msearch {} const: [{}]", context.getName(), end - start);

            for(int i=0; i<responses.size(); i++){
                Future future = context.getFutures().get(i);
                MethodMetadata methodMetadata = context.getMethodMetadata().get(i);
                String currentIndexResponse = responses.getString(i);
                Object value = methodMetadata.getResultParser().parse(currentIndexResponse, methodMetadata);
                future.setValue(value);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }finally {
            clear();
        }
    }

    public static void clear() { CONTEXT.remove(); }

    public static MSearchContext getContext() { return CONTEXT.get(); }

    public static List<Future> scope(String name, Runnable runnable){
        init(name);
        try{
            runnable.run();
            commit();
            return getContext().getFutures();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }finally {
            clear();
        }
    }
}
