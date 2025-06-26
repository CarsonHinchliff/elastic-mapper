package org.citi;

import org.citi.es.ESRestClientBuilder;
import org.citi.es.ESRestClientProperties;
import org.citi.factory.ESMapperFactory;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Carson
 * @created 2025/6/12 星期四 下午 06:45
 */
public class ESBatisConfiguration implements ApplicationContextAware {
    private RestClient restClient;
    private ESMapperFactory mapperFactory;
    private ESMapperDSLParser mapperDSLParser;

    public ESBatisConfiguration(ESRestClientProperties restClientProperties) {
        ESRestClientBuilder esRestClientBuilder = new ESRestClientBuilder(restClientProperties);
        this.restClient = esRestClientBuilder.build();
        this.mapperFactory = new ESMapperFactory(this.restClient);
        this.mapperDSLParser = ESMapperDSLParser.getInstance();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.mapperDSLParser.setApplicationContext(applicationContext);
    }

    public RestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ESMapperFactory getMapperFactory() {
        return mapperFactory;
    }

    public void setMapperFactory(ESMapperFactory mapperFactory) {
        this.mapperFactory = mapperFactory;
    }

    public ESMapperDSLParser getMapperDSLParser() {
        return mapperDSLParser;
    }

    public void setMapperDSLParser(ESMapperDSLParser mapperDSLParser) {
        this.mapperDSLParser = mapperDSLParser;
    }
}
