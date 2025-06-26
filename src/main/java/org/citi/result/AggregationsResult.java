package org.citi.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AggregationsResult <A, T> extends BaseResult<T> {
    private A aggregation;
}
