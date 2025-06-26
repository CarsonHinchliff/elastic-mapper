package org.citi.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.Template;
import com.jfinal.template.expr.ast.Const;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import org.citi.metadata.DSLNode;
import org.citi.metadata.NamespaceNode;

public class DSLDirective extends Directive {
    private String dslId;
    static final String NAMESPACE_DIRECTIVE_KEY = "_NAMESPACE_KEY";

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        Object namespaceValue = scope.get(NAMESPACE_DIRECTIVE_KEY);
        if (namespaceValue == null) {
            throw new ParseException("#dsl directive must be included in #namespace directive!", this.location);
        }

        NamespaceNode namespaceNode = (NamespaceNode) namespaceValue;
        Template template = new Template(env, this.stat);
        namespaceNode.addDslNode(new DSLNode(this.dslId, template));
    }

    @Override
    public void setExprList(ExprList exprList) {
        if(exprList.length() == 0){
            throw new ParseException("The parameter of #dsl directive can not be blank!", this.location);
        }else if (exprList.length() > 1) {
            throw new ParseException("Only one parameter of #dsl directive can be blank!", this.location);
        }

        Expr expr = exprList.getExpr(0);
        this.dslId =((Const)expr).getStr();
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
