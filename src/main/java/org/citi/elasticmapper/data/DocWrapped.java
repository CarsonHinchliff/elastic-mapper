package org.citi.elasticmapper.data;

import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:11
 */
@Data
public class DocWrapped {
    private Object doc;

    public DocWrapped(Object doc) {
        this.doc = doc;
    }
}
