package org.citi.filter;

import lombok.Data;

@Data
public class FilterItem {
    private String field;
    private String operator;
    private String value;
    private String[] multipleValue;
    private String filters;
    private Boolean enableInnerHits = false;
}
