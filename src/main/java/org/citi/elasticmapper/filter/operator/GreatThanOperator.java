package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;

public class GreatThanOperator extends OperatorAdapter {
    public GreatThanOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "gt";
    }
}
