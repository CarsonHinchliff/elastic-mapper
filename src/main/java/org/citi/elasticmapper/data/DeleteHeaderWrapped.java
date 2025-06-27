package org.citi.elasticmapper.data;

import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:11
 */
@Data
public class DeleteHeaderWrapped {
    private Object delete;

    public DeleteHeaderWrapped(Object delete) {
        this.delete = delete;
    }
}
