package org.citi.filter.operator;

import com.jfinal.template.Engine;

public class DefaultSorter extends SorterAdapter{
    public DefaultSorter(Engine engine) {
        super(engine);
    }

    @Override
    public String type() {
        return "default";
    }
}
