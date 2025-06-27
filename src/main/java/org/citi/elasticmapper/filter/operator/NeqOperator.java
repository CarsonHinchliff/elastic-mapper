package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.citi.elasticmapper.filter.FilterItemExtend;

public class NeqOperator extends OperatorAdapter{
    public NeqOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "isNotEqualTo";
    }

    @Override
    public String eval(FilterItemExtend item) {
        if ("text".equals(item.getType())){
           item.setField(item.getField() + ".keyword");
        }
        return super.eval(item);
    }
}
