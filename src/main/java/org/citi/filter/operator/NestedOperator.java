package org.citi.filter.operator;

import com.jfinal.template.Engine;
import org.citi.filter.FilterItemExtend;

import java.util.HashMap;
import java.util.Map;

public class NestedOperator extends OperatorAdapter{
    public NestedOperator(Engine engine) {
        super(engine);
    }

    @Override
    public String eval(FilterItemExtend item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String eval(FilterItemExtend item, Operator operator) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nestedPath", item.getNestedPath());
        params.put("filter", operator.eval(item));
        params.put("enableInnerHits", false);
        return this.template.renderToString(params);
    }

    public String eval(String nestedPath, String filters) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nestedPath", nestedPath);
        params.put("filter", filters);
        params.put("enableInnerHits", true);
        return this.template.renderToString(params);
    }

    @Override
    public String operator() {
        return "nested";
    }
}
