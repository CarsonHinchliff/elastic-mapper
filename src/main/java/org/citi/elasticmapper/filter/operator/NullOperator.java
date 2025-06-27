package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.citi.elasticmapper.filter.FilterItemExtend;

public class NullOperator extends OperatorAdapter{
    public NullOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String eval(FilterItemExtend item) {
        return super.eval(item);
    }

    @Override
    public String operator() {
        return "isNull";
    }
}
