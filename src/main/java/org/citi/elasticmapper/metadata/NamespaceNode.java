package org.citi.elasticmapper.metadata;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Carson
 * @created 2025/6/12 星期四 上午 11:38
 */
@Data
public class NamespaceNode {
    private String namespace;
    private final Map<String, DSLNode> dslNodeMap;

    public NamespaceNode(String namespace) {
        this.namespace = namespace;
        this.dslNodeMap = new HashMap<>();
    }

    public NamespaceNode addDslNode(DSLNode dslNode) {
        if (this.dslNodeMap.containsKey(dslNode.getId())){
            throw new IllegalArgumentException("Duplicate DSL node: " + dslNode.getId() + " in namespace: " + namespace);
        }
        this.dslNodeMap.put(dslNode.getId(), dslNode);
        return this;
    }

    public void merge(NamespaceNode namespaceNode) {
        if(!this.namespace.equals(namespaceNode.getNamespace())) {
            throw new IllegalArgumentException("Namespace mismatch: " + namespaceNode.getNamespace() + " != " + namespaceNode.getNamespace());
        }
        this.dslNodeMap.putAll(namespaceNode.getDslNodeMap());
    }
}
