package org.citi.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DeleteResult implements OriginalResult{
    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_type")
    private String type;
    @JSONField(name = "_id")
    private String id;
    @JSONField(name = "_version")
    private String version;
    private String result;
    @JSONField(name = "_shards")
    private Shards shards;
    @JSONField(name = "_seq_no")
    private Integer seqNo;
    @JSONField(name = "_primary_term")
    private Integer primaryTerm;
}
