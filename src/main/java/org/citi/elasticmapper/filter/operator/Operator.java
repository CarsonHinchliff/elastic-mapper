package org.citi.elasticmapper.filter.operator;

import org.citi.elasticmapper.filter.FilterItemExtend;

public interface Operator {
    String eval(FilterItemExtend item);
    String operator();
}
