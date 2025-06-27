package org.citi.elasticmapper.filter.operator;

import org.citi.elasticmapper.filter.SortItemExtend;

public interface Sorter {
    String type();
    String eval(SortItemExtend item);
}
