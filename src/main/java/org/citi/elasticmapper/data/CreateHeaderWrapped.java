package org.citi.elasticmapper.data;

import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:10
 */
@Data
public class CreateHeaderWrapped {
    private Object create;

    public CreateHeaderWrapped(Object create) {
        this.create = create;
    }
}
