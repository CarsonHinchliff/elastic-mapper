package org.citi.elasticmapper.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.citi.elasticmapper.metadata.MethodMetadata;
import org.citi.elasticmapper.result.PageResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Carson
 * @created 2025/6/20 星期五 下午 05:42
 */
public class PageableResultParser implements ResultParser {
    @Override
    public Object parse(String body, MethodMetadata methodMetadata) {
        PageResult pageResult = new PageResult();
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject hitsObject = jsonObject.getJSONObject("hits");
        long total = hitsObject.getJSONObject("total").getLong("value");
        pageResult.setTotal(total);
        JSONArray hitsArray = hitsObject.getJSONArray("hits");
        if (null == hitsArray || 0 == hitsArray.size()) {
            pageResult.setData(Collections.EMPTY_LIST);
            return pageResult;
        }

        // get first sort
        JSONArray firstSortArray = hitsArray.getJSONObject(0).getJSONArray("sort");
        if (null != firstSortArray){
            String[] sort = new String[firstSortArray.size()];
            for(int i=0;i<firstSortArray.size();i++){
                sort[i] = firstSortArray.getString(i);
            }
            pageResult.setFirstSort(sort);
        }

        // get last sort
        JSONArray lastSortArray = null;
        List<Object> data = new ArrayList<>();
        for(int i=0;i<hitsArray.size();i++){
            JSONObject hit = hitsArray.getJSONObject(i);
            JSONObject sourceItem = hit.getJSONObject("_source");
            Object item = sourceItem.toJavaObject(methodMetadata.getReturnType());
            data.add(item);
            lastSortArray = hit.getJSONArray("sort");
        }

        if (null != lastSortArray){
            String[] sort = new String[lastSortArray.size()];
            for(int i=0;i<lastSortArray.size();i++){
                sort[i] = lastSortArray.getString(i);
            }
            pageResult.setLastSort(sort);
        }
        pageResult.setData(data);
        return pageResult;
    }
}
