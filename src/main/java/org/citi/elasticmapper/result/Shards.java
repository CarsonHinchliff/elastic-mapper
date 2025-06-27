package org.citi.elasticmapper.result;

import lombok.Data;

@Data
public class Shards {
    private Integer total;
    private Integer successful;
    private Integer failed;
    private Integer skipped;
}
