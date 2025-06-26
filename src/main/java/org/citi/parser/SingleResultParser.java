package org.citi.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.citi.metadata.MethodMetadata;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:53
 */
public class SingleResultParser implements ResultParser{
    @Override
    public Object parse(String body, MethodMetadata methodMetadata) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        if (jsonObject.containsKey("hits")){
            JSONObject hitsObject = jsonObject.getJSONObject("hits");
            JSONArray hitsArray = hitsObject.getJSONArray("hits");
            if (null == hitsArray || hitsArray.isEmpty()){
                return null;
            } else if(hitsArray.size() > 1){
                throw new RuntimeException("expect one but return " + hitsArray.size());
            }

            JSONObject hitItem = hitsArray.getJSONObject(0).getJSONObject("_source");
            return null != methodMetadata.getReturnType() ? hitItem.toJavaObject(methodMetadata.getReturnType())
                    : hitItem.toJavaObject(methodMetadata.getResturnClass());
        } else {
            return null != methodMetadata.getReturnType() ? jsonObject.toJavaObject(methodMetadata.getReturnType())
                    : jsonObject.toJavaObject(methodMetadata.getResturnClass());
        }
    }
}
