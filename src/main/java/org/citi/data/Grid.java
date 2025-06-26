package org.citi.data;

import lombok.Data;

import java.util.List;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:12
 */
@Data
public class Grid {
    private String name;
    private List<Column> columns;
}
