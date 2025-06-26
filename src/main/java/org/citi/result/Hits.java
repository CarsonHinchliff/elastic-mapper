package org.citi.result;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class Hits <T> {
    private Total total;
    @JSONField(name = "max_score")
    private Double maxScore;
    private List<HitItem<T>> hits;
}
