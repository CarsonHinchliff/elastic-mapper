package org.citi.data;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BulkUpdateHeader extends BulkHeader{
    @JSONField(name = "retry_on_conflict")
    private Integer retryOnConflict;
}
