package org.citi.filter.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang3.StringUtils;
import org.citi.filter.SortItem;
import org.citi.filter.SortItemExtend;
import org.citi.filter.cache.IndexMappingCache;
import org.citi.filter.operator.SortRoutes;
import org.citi.infra.MapperRuntimeContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SortDirective extends Directive {
    private Expr sorts;
    private String sortType;

    @Override
    public void setExprList(ExprList exprList) {
        if (exprList.length() == 0) throw new ParseException("The parameter of #sorts directive can not be blank", this.location);

        this.sorts = exprList.getExpr(0);
        this.sortType = "default";
        if (exprList.length() > 1){
            this.sortType = ((Const)exprList.getExpr(1)).getStr();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            Object sortsValue = this.sorts.eval(scope);
            List<SortItemExtend> sortItemExtendList;
            if (sortsValue.getClass().isArray()){
                SortItem[] sortItems = (SortItem[]) sortsValue;
                sortItemExtendList = this.map(sortItems);
            } else {
                List<SortItem> sortItems = (List<SortItem>) sortsValue;
                sortItemExtendList = this.map(sortItems);
            }

            String sortsStatement = this.exec(sortItemExtendList);
            writer.write(sortsStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String exec(List<SortItemExtend> sortItemExtendList) {
        StringBuilder statement = new StringBuilder();
        sortItemExtendList.forEach(sortItemExtend -> {
            statement.append(SortRoutes.getInstance().eval(sortItemExtend, this.sortType));
            statement.append(",");
        });

        return "[" + StringUtils.removeEnd(statement.toString(), ",") + "]";
    }

    private List<SortItemExtend> map(SortItem[] sortItems){
        if (sortItems == null || sortItems.length == 0) return Collections.emptyList();

        List<SortItem> sortItemList = new ArrayList<>();
        Collections.addAll(sortItemList,sortItems);
        return this.map(sortItemList);
    }

    private List<SortItemExtend> map(List<SortItem> sortItems){
        if (sortItems == null || sortItems.isEmpty()) return Collections.emptyList();

        String index = MapperRuntimeContext.getIndex();
        IndexMappingCache indexMappingCache = IndexMappingCache.getInstance();
        return sortItems.stream().map(sortItem -> {
            SortItemExtend sortItemExtend = new SortItemExtend();
            sortItemExtend.setSortBy(StringUtils.defaultString(sortItem.getSortBy(), "asc"));
            String field = sortItemExtend.getField();
            String type = indexMappingCache.getField(index, field).getType();
            if ("text".equals(type)){
                field += ".keyword";
            }
            sortItemExtend.setField(field);
            sortItemExtend.setType(type);
            return sortItemExtend;
        }).collect(Collectors.toList());
    }
}
