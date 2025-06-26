package org.citi.filter.cache;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {
    private String key;
    private String type;
    private boolean nested;
    private String nestedPath;
}
