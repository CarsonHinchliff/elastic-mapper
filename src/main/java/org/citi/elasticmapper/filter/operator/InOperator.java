package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.citi.elasticmapper.filter.FilterItemExtend;

public class InOperator extends OperatorAdapter {
    public InOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String eval(FilterItemExtend item) {
        if ("text".equals(item.getType())) {
            String field = item.getField() + ".keyword";
            item.setField(field);
        }

        if (ArrayUtils.isEmpty(item.getMultipleValue()) && null != item.getValue()){
            item.setMultipleValue(StringUtils.split(item.getValue(), ","));
        }
        return super.eval(item);
    }

    @Override
    public String operator() {
        return "in";
    }
}
