package org.citi.filter.operator;

import org.citi.filter.SortItemExtend;

public interface Sorter {
    String type();
    String eval(SortItemExtend item);
}
