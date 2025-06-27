package org.citi.elasticmapper.infra;

public class MapperRuntimeContext {
    private static final ThreadLocal<String> CURRENT_INDEX = new ThreadLocal<>();

    public static String getIndex() {
        return CURRENT_INDEX.get();
    }

    public static void setIndex(final String index) {
        CURRENT_INDEX.set(index);
    }

    public static void removeIndex() {
        CURRENT_INDEX.remove();
    }
}
