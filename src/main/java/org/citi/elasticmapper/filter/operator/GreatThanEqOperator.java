package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;

public class GreatThanEqOperator extends OperatorAdapter {
    public GreatThanEqOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "gte";
    }
}
