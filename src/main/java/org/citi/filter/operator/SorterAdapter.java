package org.citi.filter.operator;

import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.citi.filter.SortItemExtend;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public abstract class SorterAdapter implements Sorter {
    protected final Template template;

    protected SorterAdapter(Engine engine) {
        String path = "sort/" + this.type() + ".dsl";
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
    public String eval(SortItemExtend item) {
        return this.template.renderToString(Collections.singletonMap("params", item));
    }
}
