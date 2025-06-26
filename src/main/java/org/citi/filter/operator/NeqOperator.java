package org.citi.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.StringUtils;
import org.citi.filter.FilterItemExtend;

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
