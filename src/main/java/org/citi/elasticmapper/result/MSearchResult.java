package org.citi.elasticmapper.result;

import lombok.Data;

import java.util.List;

@Data
public class MSearchResult <T> implements OriginalResult{
    private Long took;
    private List<BaseResult<T>> responses;
}
