package org.citi.elasticmapper.executor;

import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.elasticmapper.infra.Future;
import org.citi.elasticmapper.infra.MSearch;
import org.citi.elasticmapper.infra.MSearchContext;
import org.citi.elasticmapper.infra.MapperRuntimeContext;
import org.citi.elasticmapper.metadata.MethodMetadata;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 08:29
 */
@Slf4j
public class MapperGetExecutor implements MapperMethodExecutor {
    private final Template template;
    private final MethodMetadata methodMetadata;

    public MapperGetExecutor(Template template, MethodMetadata methodMetadata) {
        this.template = template;
        this.methodMetadata = methodMetadata;
    }

    @Override
    public Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable {
        index = index == null ? this.methodMetadata.getDefaultIndex() : index;
        MapperRuntimeContext.setIndex(index);
        try {
            Map<String, Object> params = new HashMap<>();

            for(int i=0; i<args.length; i++) {
                params.put(this.methodMetadata.getParamsName().get(i), args[i]);
            }

            String dsl = this.template.renderToString(params);
            MSearchContext mSearchContext = MSearch.getContext();
            if (null != mSearchContext) {
                return mSearchProcess(mSearchContext, index, dsl, restClient);
            }
            return defaultProcess(index, dsl, restClient, method.getName());
        } finally {
            MapperRuntimeContext.removeIndex();
        }
    }

    private Object mSearchProcess(MSearchContext mSearchContext, String index, String dsl, RestClient restClient){
        mSearchContext.setRestClient(restClient);
        String targetDSL = StringUtils.trim(dsl).replaceAll("[\r\n]", "");
        mSearchContext.getMSearchStatement().append("{\"index\": ").append("\"").append(index)
                .append("\"").append("}").append("\r\n").append(targetDSL).append("\r\n");
        mSearchContext.getMethodMetadata().add(this.methodMetadata);
        Future future = new Future();
        mSearchContext.getFutures().add(future);
        return null;
    }

    private Object defaultProcess(String index, String dsl, RestClient restClient, String methodName) throws IOException {
        Request request = new Request("GET", "/" + index + "/_search");
        request.setJsonEntity(dsl);
        long start = System.currentTimeMillis();
        Response response = restClient.performRequest(request);
        long end = System.currentTimeMillis();
        log.info("execute {} cost [{}ms]", methodName, end - start);
        String responseBody = EntityUtils.toString(response.getEntity());
        return this.methodMetadata.getResultParser().parse(responseBody, this.methodMetadata);
    }
}
