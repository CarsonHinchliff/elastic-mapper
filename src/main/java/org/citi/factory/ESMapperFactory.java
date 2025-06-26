package org.citi.factory;

import org.citi.proxy.ESMapperProxy;
import org.citi.proxy.RestClientProxy;
import org.elasticsearch.client.RestClient;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:17
 */
public class ESMapperFactory {
    private RestClientProvider restClientProvider;

    public ESMapperFactory(RestClient restClient) {
        RestClientProvider.getInstance().setRestClient(restClient);
    }

    public ESMapperFactory(RestClient restClient, RestClientProxy restClientProxy) {
        RestClientProvider.getInstance().setRestClient(restClient);
        RestClientProvider.getInstance().setRestClientProxy(restClientProxy);
    }

    public <T> T getMapper(Class<T> clazz) {
        return (T)(new ESMapperProxy(RestClientProvider.getInstance())).proxy(clazz);
    }
}
