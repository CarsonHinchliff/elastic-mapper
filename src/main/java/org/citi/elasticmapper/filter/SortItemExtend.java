package org.citi.elasticmapper.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SortItemExtend extends SortItem{
    private String type;
}
