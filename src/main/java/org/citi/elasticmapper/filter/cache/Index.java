package org.citi.elasticmapper.filter.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Index {
    private String index;
    private Map<String, Field> fieldMap;

    public Index(String index) {
        this.index = index;
        this.fieldMap = new HashMap<>();
    }

    public Index addField(Field field) {
        this.fieldMap.put(field.getKey(), field);
        return this;
    }
}
