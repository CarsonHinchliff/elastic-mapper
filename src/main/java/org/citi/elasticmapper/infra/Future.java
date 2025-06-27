package org.citi.elasticmapper.infra;

import lombok.Data;

@Data
public class Future<T> {
    private T value;
}
