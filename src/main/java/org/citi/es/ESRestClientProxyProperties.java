package org.citi.es;

import lombok.Data;

import java.util.List;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:31
 */
@Data
public class ESRestClientProxyProperties {
    private String defaultClient;
    private List<ESRestClientProperties> clients;
}
