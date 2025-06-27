package org.citi.elasticmapper.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author Carson
 * @created 2025/6/12 星期四 下午 06:28
 */
public class ShareUtils {
    public static int length(Collection collection){
        return null == collection ? 0 : collection.size();
    }

    public static int length(String string){
        return StringUtils.length(string);
    }

    public static int length(Object object){
        return Array.getLength(object);
    }

    public static <T> boolean isEmpty(T[] array){
        return null == array || array.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> collection){
        return null == collection || collection.size() == 0;
    }

    public static <T> boolean notEmpty(T[] array){
        return !isEmpty(array);
    }
}
