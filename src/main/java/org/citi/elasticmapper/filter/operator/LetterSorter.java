package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.StringUtils;
import org.citi.elasticmapper.filter.SortItemExtend;

import java.util.Collections;

public class LetterSorter extends SorterAdapter{
    private final DefaultSorter defaultSorter;

    public LetterSorter(Engine engine) {
        super(engine);
        this.defaultSorter = new DefaultSorter(engine);
    }

    @Override
    public String eval(SortItemExtend item) {
        if (!"text".equals(item.getType()) && !"keyword".equals(item.getType())) {
            return defaultSorter.eval(item);
        } else {
            String field = StringUtils.removeEnd(item.getField(), ".keyword");
            item.setField(field);
            return this.template.renderToString(Collections.singletonMap("params", item));
        }
    }

    @Override
    public String type() {
        return "letter";
    }
}
