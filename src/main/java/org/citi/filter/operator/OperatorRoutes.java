package org.citi.filter.operator;

import com.jfinal.template.Engine;
import org.apache.commons.lang3.StringUtils;
import org.citi.constant.Constants;
import org.citi.filter.FilterItemExtend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperatorRoutes {
    private final NestedOperator nestedOperator;
    private final Map<String, Operator> operators;

    private OperatorRoutes(){
        Engine engine = Engine.use(Constants.EngineConstants.ENGINE);
        this.operators = new HashMap<>();
        this.nestedOperator = new NestedOperator(engine);
        this.addOperator(new ContainsOperator(engine));
        this.addOperator("Contains", new ContainsOperator(engine));
        this.addOperator(new EqOperator(engine));
        this.addOperator("Eq", new EqOperator(engine));
        this.addOperator(new GreatThanEqOperator(engine));
        this.addOperator("GreatThanEq", new GreatThanOperator(engine));
        this.addOperator(new GreatThanOperator(engine));
        this.addOperator("GreatThan", new GreatThanOperator(engine));
        this.addOperator(new LessThanEqOperator(engine));
        this.addOperator("LessThanEq", new LessThanOperator(engine));
        this.addOperator(new LessThanOperator(engine));
        this.addOperator("LessThan", new LessThanOperator(engine));
        this.addOperator(new StartWithOperator(engine));
        this.addOperator("StartWith", new StartWithOperator(engine));
        this.addOperator(new EndWithOperator(engine));
        this.addOperator("EndWith", new EndWithOperator(engine));
        this.addOperator(new InOperator(engine));
        this.addOperator("In", new InOperator(engine));
        this.addOperator(new BetweenOperator(engine));
        this.addOperator("Between", new BetweenOperator(engine));
        this.addOperator(new NullOperator(engine));
        this.addOperator("Null", new NullOperator(engine));
        this.addOperator(new NeqOperator(engine));
        this.addOperator("Neq", new NeqOperator(engine));
    }

    public void addOperator(Operator operator){
        this.operators.put(operator.operator(), operator);
    }

    public void addOperator(String operatorKey, Operator operator){
        this.operators.put(operatorKey, operator);
    }

    public String eval(FilterItemExtend filterItemExtend){
        Operator operator = operators.get(filterItemExtend.getOperator());
        if (operator == null){
            throw new IllegalArgumentException("Operator not found: " + filterItemExtend.getOperator());
        }
        return filterItemExtend.isNested() ? this.nestedOperator.eval(filterItemExtend, operator) : operator.eval(filterItemExtend);
    }

    public String eval(String nestedPath, List<FilterItemExtend> filterItemExtends){
        StringBuilder statement = new StringBuilder();
        filterItemExtends.forEach(filterItemExtend -> {
            Operator operator = operators.get(filterItemExtend.getOperator());
            if (operator == null){ throw new IllegalArgumentException("Operator not found: " + filterItemExtend.getOperator()); }
            statement.append(operator.eval(filterItemExtend));
            statement.append(",");
        });
        String filters = StringUtils.removeEnd(statement.toString(), ",");
        return this.nestedOperator.eval(nestedPath, filters);
    }

    public String operator() { return ""; }

    public static OperatorRoutes getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder{
        private static final OperatorRoutes INSTANCE = new OperatorRoutes();
        private SingletonHolder(){}
    }
}
