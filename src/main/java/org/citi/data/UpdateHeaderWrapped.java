package org.citi.data;

import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:13
 */
@Data
public class UpdateHeaderWrapped {
    private Object update;

    public UpdateHeaderWrapped(Object update) {
        this.update = update;
    }
}
