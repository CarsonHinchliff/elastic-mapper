package org.citi.elasticmapper.filter.operator;

import com.jfinal.template.Engine;
import org.citi.elasticmapper.constant.Constants;
import org.citi.elasticmapper.filter.SortItemExtend;

import java.util.HashMap;
import java.util.Map;

public class SortRoutes {
    private final Map<String, Sorter> sorters;

    private SortRoutes() {
        Engine engine = Engine.use(Constants.EngineConstants.ENGINE);
        this.sorters = new HashMap<String, Sorter>();
        LetterSorter letterSorter = new LetterSorter(engine);
        DefaultSorter defaultSorter = new DefaultSorter(engine);
        this.sorters.put(letterSorter.type(), letterSorter);
        this.sorters.put(defaultSorter.type(), defaultSorter);
    }

    public String eval(SortItemExtend item, String type){
        return sorters.get(type).eval(item);
    }

    public static SortRoutes getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder{
        private static final SortRoutes INSTANCE = new SortRoutes();

        private SingletonHolder(){}
    }
}
