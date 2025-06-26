package org.citi.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.TemplateException;
import com.jfinal.template.io.CharWriter;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

import java.io.CharArrayWriter;
import java.io.IOException;

public class CompactDirective extends Directive {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        try {
            CharWriter charWriter = new CharWriter(128);
            CharArrayWriter charArrayWriter = new CharArrayWriter();
            charWriter.init(charArrayWriter);
            this.stat.exec(env, scope, writer);
            String result = charArrayWriter.toString().replaceAll("[\\r\\n]", "");
            charArrayWriter.close();
            writer.write(result);
        }catch (IOException ex){
            throw new TemplateException(ex.getMessage(), this.stat.getLocation());
        }
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
