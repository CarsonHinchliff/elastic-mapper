package org.citi.factory;

import org.citi.proxy.RestClientProxy;
import org.elasticsearch.client.RestClient;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:13
 */
public class RestClientProvider {
    private RestClient restClient;
    private RestClientProxy restClientProxy;

    private RestClientProvider(){
        throw new IllegalStateException("Utility class");
    }

    public RestClient getRestClient() {
        return null == this.restClientProxy ? this.restClient : this.restClientProxy.getClientByContext();
    }

    public static RestClientProvider getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void setRestClient(final RestClient restClient) {
        this.restClient = restClient;
    }

    public void setRestClientProxy(final RestClientProxy restClientProxy) {
        this.restClientProxy = restClientProxy;
    }

    private static class SingletonHolder {
        private static final RestClientProvider INSTANCE = new RestClientProvider();
        private SingletonHolder() {
            throw new IllegalStateException("Utility class");
        }
    }
}
