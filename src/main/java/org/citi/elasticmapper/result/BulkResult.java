package org.citi.elasticmapper.result;

import lombok.Data;

import java.util.List;

@Data
public class BulkResult <T> implements OriginalResult{
    private Integer took;
    private Boolean errors;
    private List<T> items;
}
