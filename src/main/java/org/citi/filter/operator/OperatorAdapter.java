package org.citi.filter.operator;

import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.citi.filter.FilterItemExtend;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public abstract class OperatorAdapter implements Operator{
    protected final Template template;

    protected OperatorAdapter(Engine engine) {
        String path = "operator/" + this.operator() + ".dsl";
        String content = this.loadTemplateFile(path);
        this.template = engine.getTemplateByString(content);
    }

    private String loadTemplateFile(String path) {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
            return FileCopyUtils.copyToString(new InputStreamReader(inputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String eval(FilterItemExtend item) {
        return this.template.renderToString(Collections.singletonMap("params", item));
    }
}
