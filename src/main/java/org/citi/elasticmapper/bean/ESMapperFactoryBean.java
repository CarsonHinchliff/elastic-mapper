package org.citi.elasticmapper.bean;

import org.citi.elasticmapper.factory.RestClientProvider;
import org.citi.elasticmapper.proxy.ESMapperProxy;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:14
 */
public class ESMapperFactoryBean implements FactoryBean {
    private Class<?> mapperClazz;

    public Class<?> getMapperClazz() {
        return mapperClazz;
    }

    public void setMapperClazz(Class<?> mapperClazz) {
        this.mapperClazz = mapperClazz;
    }

//    @SuppressWarnings("unchecked")
    @Override
    public Object getObject() throws Exception {
        return new ESMapperProxy(RestClientProvider.getInstance()).proxy(mapperClazz);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
