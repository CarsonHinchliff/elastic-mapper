package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;

public class LessThanOperator extends OperatorAdapter {
    public LessThanOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "lt";
    }
}
