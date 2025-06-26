package org.citi.filter.cache;

import com.alibaba.fastjson.JSONObject;
import jakarta.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.citi.factory.RestClientProvider;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class IndexMappingCache {
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, Index> indcies = new HashMap<>();
    private final RestClientProvider restClientProvider = RestClientProvider.getInstance();

    private IndexMappingCache(){}

    public Field getField(String index, String field){
        String[] indcies = StringUtils.split(index,",");
        for(String indcie: indcies){
            Index indexData = this.indcies.computeIfAbsent(indcie, this::loadIndex);
            Field fieldData = indexData.getFieldMap().get(field);
            if (null != fieldData) {
                return fieldData;
            }
        }
        return Field.builder().key(field).nested(false).type("text").build();
    }

    private Index loadIndex(String index){
        this.lock.lock();
        try {
            if (this.indcies.containsKey(index)) { return this.indcies.get(index); }

            Request request = new Request("GET", "/" + index + "_mapping");
            Response response = restClientProvider.getRestClient().performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject mappings = JSONObject.parseObject(responseBody);
            Index indexData = new Index(index);
            mappings.forEach((curIndex, value) -> {
                JSONObject properties = ((JSONObject) value).getJSONObject("mappings").getJSONObject("properties");
                readFields(null, properties, indexData);
            });
            return indexData;
        } catch (Exception e) {
            log.error("init index[{}] cache error", index, e);
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }
    }

    private void readFields(String path, JSONObject properties, Index indexData){
        properties.forEach((key, value) -> {
            JSONObject mappings = (JSONObject) value;
            String type = (String) mappings.get("type");
            String keyName = StringUtils.isNotBlank(path) ? path + "." + key : key;
            if ("nested".equals(type)) {
                JSONObject nestedProperties = (JSONObject) mappings.get("properties");
                nestedProperties.forEach((nKey, nValue) -> {
                    String fieldKeyName = keyName + "." + nKey;
                    String nestedKeyType = ((JSONObject) nValue).getString("type");
                    Field field = Field.builder().type(nestedKeyType).nested(true).nestedPath(keyName).key(fieldKeyName).build();
                    indexData.addField(field);
                });
            } else if(mappings.containsKey("properties")){
                this.readFields(keyName, mappings.getJSONObject("properties"), indexData);
            } else {
                Field field = Field.builder().key(keyName).type(type).nested(false).build();
                indexData.addField(field);
            }
        });
    }

    public static  IndexMappingCache getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder{
        private static final IndexMappingCache INSTANCE = new IndexMappingCache();
        private SingletonHolder() {}
    }
}
