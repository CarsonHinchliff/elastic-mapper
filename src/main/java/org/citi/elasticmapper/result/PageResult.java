package org.citi.elasticmapper.result;

import lombok.Data;

import java.util.List;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:42
 */
@Data
public class PageResult<T> implements Pageable<T> {
    long total;
    String[] firstSort;
    String[] lastSort;
    List<T> data;
}
