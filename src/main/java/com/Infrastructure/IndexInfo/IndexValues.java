package com.Infrastructure.IndexInfo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/26
 * @description:
 */
@Data
public class IndexValues {

    private String indexName;

    //内部包含，左右
    private List<String> keyPoint;

    private IndexInfo indexInfo;

    public IndexValues(String name) {
        this.indexName = name;
        this.keyPoint = new ArrayList<>();
    }

    public IndexValues(String name,IndexInfo indexInfo){
        this.indexName = name;
        this.indexInfo = indexInfo;
    }

    public void addPoint(String left,String right) {
        this.keyPoint.add(left);
        this.keyPoint.add(right);
    }

}
