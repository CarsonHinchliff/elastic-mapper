package org.citi.elasticmapper.result;

import java.util.List;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:35
 */
public interface Pageable<T>{
    long getTotal();
    String[] getFirstSort();
    String[] getLastSort();
    List<T> getData();
}
