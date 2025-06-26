package org.citi.es;

import lombok.Data;

import java.util.List;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:32
 */
@Data
public class ESRestClientProperties {
    private List<ESProperties> connections;
    private String username;
    private String password;
    private Integer asyncThreadCount;
    private Integer connectTimeout = 5000;
    private Integer socketTimeout = 60000;
    private Long sniffRequestTimeoutMillis;
    private String name;
    private String apiKey;
    private String apiKeyAuth;
    private String trustKeyStore;
    private String trustKeyStorePwd;
}
