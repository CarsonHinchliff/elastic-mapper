package org.citi.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:04
 */
@Data
public class BulkItem<T extends BulkHeader> {
    @JSONField(name = "index")
    private T header;
    private Object data;
    private BulkOperate operate;
}
