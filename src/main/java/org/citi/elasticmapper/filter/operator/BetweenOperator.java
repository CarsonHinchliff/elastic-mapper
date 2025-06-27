package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.ArrayUtils;
import org.citi.elasticmapper.filter.FilterItemExtend;

import java.util.Collections;

public class BetweenOperator extends OperatorAdapter{
    public BetweenOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String eval(FilterItemExtend item) {
        if (ArrayUtils.isEmpty(item.getMultipleValue()) && null != item.getValue()) {
            String[] multipleValues = new String[]{item.getValue()};
            item.setMultipleValue(multipleValues);
        }
        return this.template.renderToString(Collections.singletonMap("params", item));
    }

    @Override
    public String operator() {
        return "between";
    }
}
