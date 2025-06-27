package org.citi.elasticmapper.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:05
 */
@Data
public class BulkHeader {
    @JSONField(name = "_index")
    private String index;
    @JSONField(name = "_id")
    private String id;
}
