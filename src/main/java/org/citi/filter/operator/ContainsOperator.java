package org.citi.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.StringUtils;
import org.citi.filter.FilterItemExtend;

public class ContainsOperator extends OperatorAdapter{
    public ContainsOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String operator() {
        return "contains";
    }

    @Override
    public String eval(FilterItemExtend item) {
        if ("text".equals(item.getType())){
            item.setValue(StringUtils.toRootLowerCase(item.getValue()));
        }
        return super.eval(item);
    }
}
