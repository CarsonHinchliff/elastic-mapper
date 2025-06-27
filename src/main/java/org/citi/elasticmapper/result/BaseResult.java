package org.citi.elasticmapper.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BaseResult <T> implements OriginalResult{
    private Integer took;
    @JSONField(name = "time_out")
    private boolean timeout;
    @JSONField(name = "_shards")
    private Shards shards;
    private Hits<T> hits;
    private Integer total = 0;
    private Integer deleted = 0;
    private Integer batches = 0;
}
