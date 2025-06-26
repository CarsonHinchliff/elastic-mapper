package org.citi.data;

/**
 * @author Carson
 * @created 2025/6/21 星期六 上午 09:06
 */

public enum BulkOperate {
    CREATE("update"),
    UPDATE("update"),
    DELETE("delete");
    private String value;

    public String getValue() {
        return value;
    }

    BulkOperate(String value) {
        this.value = value;
    }
}
