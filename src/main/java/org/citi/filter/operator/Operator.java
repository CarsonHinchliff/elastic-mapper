package org.citi.filter.operator;

import org.citi.filter.FilterItemExtend;

public interface Operator {
    String eval(FilterItemExtend item);
    String operator();
}
