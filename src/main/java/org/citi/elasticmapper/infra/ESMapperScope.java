package org.citi.elasticmapper.infra;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

public class ESMapperScope {
    private static ThreadLocal<String> CURRENT_INDEX = new ThreadLocal<>();

    public static <T> T scope(String index, Supplier<T> supplier) {
        Object result = null;
        try {
            CURRENT_INDEX.set(index);
            result = supplier.get();
        } finally {
            CURRENT_INDEX.remove();
        }
        return (T) result;
    }

    public static <T> T scope(String[] indices, Supplier<T> supplier) {
        String index = StringUtils.join(indices, ",");
        return scope(index, supplier);
    }

    public static String getIndex(){
        return CURRENT_INDEX.get();
    }
}
