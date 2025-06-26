package org.citi.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import org.citi.ESMapperDSLParser;
import org.citi.metadata.NamespaceNode;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:44
 */
public class NamespaceDirective extends Directive {
    private String namespace;
    static final String NAMESPACE_DIRECTIVE_KEY = "_NAMESPACE_KEY";

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        NamespaceNode namespaceNode = new NamespaceNode(this.namespace);
        scope.set(NAMESPACE_DIRECTIVE_KEY, namespaceNode);
        this.stat.exec(env, scope, writer);
        ESMapperDSLParser.getInstance().addNamespaceNode(namespaceNode);
    }

    @Override
    public void setExprList(ExprList exprList) {
        if (exprList.length() == 0){
            throw new ParseException("The parameter of #namespace directve can not be blank!", this.location);
        } else if (exprList.length() > 1 ){
            throw new ParseException("Only 1 parameter is allowed for #namespace directive!", this.location);
        }

        Expr expr = exprList.getExpr(0);
        this.namespace = ((Const)expr).getStr();
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
