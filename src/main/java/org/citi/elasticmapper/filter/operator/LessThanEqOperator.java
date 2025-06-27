package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;

public class LessThanEqOperator extends OperatorAdapter {
    public LessThanEqOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "lte";
    }
}
