package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.citi.elasticmapper.filter.FilterItemExtend;

public class EqOperator extends OperatorAdapter{
    public EqOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String eval(FilterItemExtend item) {
        if ("text".equals(item.getType())) {
            String field = item.getField() + ".keyword";
            item.setField(field);
        }
        return super.eval(item);
    }

    @Override
    public String operator() {
        return "eq";
    }
}
