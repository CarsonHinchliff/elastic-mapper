package org.citi.infra;

import lombok.Data;

@Data
public class Future<T> {
    private T value;
}
