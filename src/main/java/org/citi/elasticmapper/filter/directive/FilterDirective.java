package org.citi.elasticmapper.filter.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.citi.elasticmapper.filter.FilterItem;
import org.citi.elasticmapper.filter.FilterItemExtend;
import org.citi.elasticmapper.filter.cache.Field;
import org.citi.elasticmapper.filter.cache.IndexMappingCache;
import org.citi.elasticmapper.filter.operator.OperatorRoutes;
import org.citi.elasticmapper.infra.MapperRuntimeContext;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FilterDirective extends Directive {
    private Expr filters;

    @Override
    public void setExprList(ExprList exprList) {
        if (exprList.length() == 0){
            throw new ParseException("The parameter of #filters directive can not be blank!", this.location);
        } else if(exprList.length() > 1){
            throw new ParseException("Only one parameter of #filters directive can be specified!", this.location);
        }
        this.filters = exprList.getExpr(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            Object filtersValue = this.filters.eval(scope);
            List<FilterItemExtend> filterItemExtends = null;
            if (filtersValue.getClass().isArray()){
                FilterItem[] filterItems = (FilterItem[]) filtersValue;
                filterItemExtends = this.map(filterItems);
            } else {
                filterItemExtends = this.mapExt((List<FilterItem>)filtersValue);
            }
            String filterStatement = this.exec(filterItemExtends);
            writer.write(filterStatement);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String exec(List<FilterItemExtend> filterItems){
        StringBuilder statementBuilder = new StringBuilder();
        Map<String, List<FilterItemExtend>> nestedMap = new LinkedHashMap<>();

        filterItems.forEach(filterItem -> {
            if (filterItem.getEnableInnerHits()){
                (nestedMap.computeIfAbsent(filterItem.getNestedPath(), k -> new ArrayList<>())).add(filterItem);
            } else if (StringUtils.isBlank(filterItem.getOperator())){
                log.warn("filter[key: {}, value: {}] item operator is null", filterItem.getField(), filterItem.getValue());
            } else {
                statementBuilder.append(this.exec(filterItem));
                statementBuilder.append(",");
            }
        });

        nestedMap.forEach((key, value) -> {
           statementBuilder.append(OperatorRoutes.getInstance().eval(key, value));
           statementBuilder.append(",");
        });

        return StringUtils.removeEnd(statementBuilder.toString(), ",");
    }

    private String exec(FilterItemExtend filterItemExtend){
        return OperatorRoutes.getInstance().eval(filterItemExtend);
    }

    private List<FilterItemExtend> map(FilterItem[] filterItems){
        if (filterItems.length == 0){
            return Collections.emptyList();
        }

        List<FilterItem> filterItemList = new ArrayList<>();
        Collections.addAll(filterItemList, filterItems);
        return mapExt(filterItemList);
    }

    private List<FilterItemExtend> mapExt(List<FilterItem> filterItems){
        if (filterItems.isEmpty()){ return Collections.emptyList(); }
        String index = MapperRuntimeContext.getIndex();
        IndexMappingCache indexMappingCache = IndexMappingCache.getInstance();
        return filterItems.stream().map(filterItem -> {
            FilterItemExtend filterItemExtend = new FilterItemExtend();
            filterItemExtend.setField(filterItem.getField());
            filterItemExtend.setOperator(filterItem.getOperator());
            filterItemExtend.setValue(filterItem.getValue());
            filterItemExtend.setMultipleValue(filterItem.getMultipleValue());

            Field field = indexMappingCache.getField(index, filterItem.getField());
            filterItemExtend.setType(field.getType());
            filterItemExtend.setNested(field.isNested());
            filterItemExtend.setNestedPath(field.getNestedPath());
            filterItemExtend.setEnableInnerHits(field.isNested() && Boolean.TRUE.equals(filterItem.getEnableInnerHits()));

            return filterItemExtend;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean hasEnd() {
        return false;
    }
}
