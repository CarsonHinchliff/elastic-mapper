package org.citi.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class HitItem <T>{
    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_type")
    private String type;
    @JSONField(name = "_id")
    private String id;
    @JSONField(name = "_source")
    private T source;
    private String[] sort;
}
