package org.citi.filter.operator;

import com.jfinal.template.Engine;
import org.citi.filter.FilterItemExtend;

public class StartWithOperator extends OperatorAdapter{
    public StartWithOperator(Engine engine) {
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
        return "startWith";
    }
}
