package org.citi.executor;

import org.elasticsearch.client.RestClient;

import java.lang.reflect.Method;

/**
 * @author Carson
 * @created 2025/6/12 星期四 下午 06:24
 */
public interface MapperMethodExecutor {
    Object execute(RestClient restClient, String index, Method method, Object[] args) throws Throwable;
}
