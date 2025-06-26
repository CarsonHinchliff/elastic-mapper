package org.citi.infra;

import lombok.Data;
import org.citi.metadata.MethodMetadata;
import org.elasticsearch.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Data
public class MSearchContext {
    private String name;
    private List<Future> futures;
    private RestClient restClient;
    private StringBuilder mSearchStatement;
    private List<MethodMetadata> methodMetadata;

    public MSearchContext(String name) {
        this.name = name;
        this.futures = new ArrayList<Future>();
        this.methodMetadata = new ArrayList<>();
        this.mSearchStatement = new StringBuilder();
    }
}
