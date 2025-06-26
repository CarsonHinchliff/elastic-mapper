package org.citi.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Base64;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 10:46
 */
@Slf4j
public class ESRestClientBuilder {
    private final ESRestClientProperties properties;
    public ESRestClientBuilder(ESRestClientProperties properties) {
        this.properties = properties;
    }

    public RestClient build() {
        HttpHost[] httpHosts = new HttpHost[this.properties.getConnections().size()];
        for(int i=0; i<this.properties.getConnections().size(); i++) {
            ESProperties esProperties = this.properties.getConnections().get(i);
            httpHosts[i] = new HttpHost(esProperties.getHost(), esProperties.getPort(), esProperties.getSchema());
        }
        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts)
                .setRequestConfigCallback((builder -> {
                    return builder.setConnectTimeout(this.properties.getConnectTimeout())
                            .setSocketTimeout(this.properties.getSocketTimeout());
                }));
        if (null != this.properties.getApiKey() && null != this.properties.getApiKeyAuth()){
            String apiKeyAuth = Base64.getEncoder().encodeToString((this.properties.getApiKey() + ":" + this.properties.getApiKeyAuth())
                    .getBytes(StandardCharsets.UTF_8));
            Header[] headers = new Header[]{new BasicHeader("Authorization", "ApkKey " +  apiKeyAuth)};
            restClientBuilder.setDefaultHeaders(headers);
        }

        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                try{
                    httpAsyncClientBuilder.disableAuthCaching();
                    if (null != ESRestClientBuilder.this.properties.getUsername()
                    && null != ESRestClientBuilder.this.properties.getPassword()) {
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(ESRestClientBuilder.this.properties.getUsername(), ESRestClientBuilder.this.properties.getPassword()));
                    }
                    if (null != ESRestClientBuilder.this.properties.getTrustKeyStore()) {
                        ESRestClientBuilder.this.setKeyStoreContext(httpAsyncClientBuilder, ESRestClientBuilder.this.properties.getTrustKeyStore(), ESRestClientBuilder.this.properties.getTrustKeyStorePwd());
                    }
                    return httpAsyncClientBuilder;
                }catch (Throwable e){
                    log.error("Error when building http client {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
        restClientBuilder.setHttpClientConfigCallback(httpClientConfigCallback);
        restClientBuilder.setCompressionEnabled(true);
        return restClientBuilder.build();
    }

    private InputStream getKeyStoreInputStream(String keyStorePath) throws IOException {
        InputStream inputStream;
        if (keyStorePath.startsWith("classpath:")){
            keyStorePath = keyStorePath.replace("classpath:", "");
            return this.getClass().getClassLoader().getResourceAsStream(keyStorePath);
        }else {
            Path path = Paths.get(keyStorePath);
            return Files.newInputStream(path);
        }
    }

    private void setKeyStoreContext(HttpAsyncClientBuilder httpClientBuilder, String keyStorePath, String keyStorePassword) throws Throwable{
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        try(InputStream inputStream = getKeyStoreInputStream(keyStorePath)) {
            try {
                keyStore.load(inputStream, keyStorePassword.toCharArray());
            } catch (Exception e) {
                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e.addSuppressed(e1);
                    }
                }
                throw e;
            }
        }
        SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(keyStore, null);
        SSLContext sslContext = sslContextBuilder.build();
        httpClientBuilder.setSSLContext(sslContext);
    }
}
