package org.citi;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * @author Carson
 * @created 2025/6/12 星期四 下午 06:36
 */
public class ESMapperContext {
    private final ThreadLocal<String> CURRENT_INDEX = new ThreadLocal<>();

    private ESMapperContext(){
        throw new IllegalStateException("Utility class");
    }

    public static ESMapperContext getInstance(){
        return ESMapperContext.getInstance();
    }

    public <T> T run(String index, Supplier<T> supplier){
        T result = null;
        try{
            CURRENT_INDEX.set(index);
            result = supplier.get();
        } finally {
            CURRENT_INDEX.remove();
        }
        return result;
    }

    public <T> T run(String[] indexes, Supplier<T> supplier){
        T result = null;
        try{
            CURRENT_INDEX.set(StringUtils.join(indexes, ","));
            result = supplier.get();
        } finally {
            CURRENT_INDEX.remove();
        }
        return result;
    }

    public String getCurrentIndex(){
        return CURRENT_INDEX.get();
    }

    private static class SingletonHolder{
        private static final ESMapperContext INSTANCE = new ESMapperContext();
        private SingletonHolder(){ throw new IllegalStateException("Utility class"); }
    }
}
