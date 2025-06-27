package org.citi.elasticmapper.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.citi.elasticmapper.metadata.MethodMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:37
 */
public class CollectionResultParser implements ResultParser{
    @Override
    public Object parse(String body, MethodMetadata methodMetadata) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject hitsObject = jsonObject.getJSONObject("hits");
        JSONArray hitsArray = hitsObject.getJSONArray("hits");
        if (hitsArray == null || hitsArray.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < hitsArray.size(); i++) {
            JSONObject hit = hitsArray.getJSONObject(i);
            JSONObject hitObject = hit.getJSONObject("_source");
            Object item = hitObject.toJavaObject(methodMetadata.getReturnType());
            list.add(item);
        }
        return list;
    }
}
