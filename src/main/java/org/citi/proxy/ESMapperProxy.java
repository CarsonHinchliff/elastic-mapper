package org.citi.proxy;


import lombok.extern.slf4j.Slf4j;
import org.citi.ESMapperContext;
import org.citi.ESMapperDSLParser;
import org.citi.executor.MapperMethodExecutor;
import org.citi.factory.RestClientProvider;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:26
 */
@Slf4j
public class ESMapperProxy<T> implements InvocationHandler {
    private Class<?> mapperClazz;
    private RestClientProvider restClientProvider;

    public ESMapperProxy(RestClientProvider restClientProvider) {
        this.restClientProvider = restClientProvider;
    }

    public void setMapperClazz(Class<?> mapperClazz) {
        this.mapperClazz = mapperClazz;
    }

    public void setRestClientProvider(RestClientProvider restClientProvider) {
        this.restClientProvider = restClientProvider;
    }

    public T proxy(Class<T> mapperClazz){
        this.mapperClazz = mapperClazz;
        ESMapperDSLParser.getInstance().addMapper(mapperClazz);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(mapperClazz);
        enhancer.setCallback(this);
        return (T)enhancer.create();
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        MapperMethodExecutor executor = (MapperMethodExecutor)ESMapperDSLParser.getInstance().getMapperMethodExecutorMap().get(method);
        return executor.execute(this.restClientProvider.getRestClient(), ESMapperContext.getInstance().getCurrentIndex(), method, objects);
    }
}
