package org.citi.proxy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.citi.es.ESRestClientBuilder;
import org.citi.es.ESRestClientProxyProperties;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:27
 */
@Slf4j
public class RestClientProxy {
   private static final ThreadLocal<String> context = new ThreadLocal<>();

   private String defaultClient;
   private ESRestClientProxyProperties clientProxyProperties;
   private Map<String, RestClient> restClientMap;


    public ESRestClientProxyProperties getClientProxyProperties() {
        return clientProxyProperties;
    }

    public void setClientProxyProperties(ESRestClientProxyProperties clientProxyProperties) {
        this.clientProxyProperties = clientProxyProperties;
    }

    public RestClientProxy(ESRestClientProxyProperties clientProxyProperties) {
        this.clientProxyProperties = clientProxyProperties;
        this.restClientMap = new HashMap<>();
    }

    public RestClient getClient(String name){
        return restClientMap.get(name);
    }

    public RestClient getClientByContext(){
        String clientName = context.get();
        if (clientName == null) { return defaultClient();}
        return getClient(clientName);
    }

    public RestClient defaultClient(){
        return this.restClientMap.get(this.defaultClient);
    }

    public void init(){
        this.defaultClient = this.clientProxyProperties.getDefaultClient();
        this.clientProxyProperties.getClients().forEach(client -> {
            ESRestClientBuilder restClientBuilder = new ESRestClientBuilder(client);
            RestClient restClient = restClientBuilder.build();
            this.restClientMap.put(client.getName(), restClient);
        });
    }

    public void shutdown(){
        this.restClientMap.forEach((clientName, restClient) -> {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("close restClient[{}] error:", clientName, e);
            }
        });
    }
}
