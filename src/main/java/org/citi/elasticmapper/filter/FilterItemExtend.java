package org.citi.elasticmapper.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FilterItemExtend extends FilterItem{
    private String type;
    private boolean nested;
    private String nestedPath;
}
