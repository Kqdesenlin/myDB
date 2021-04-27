package com.Infrastructure.IndexInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: zhangQY
 * @date: 2021/4/27
 * @description:
 */
@Data
@AllArgsConstructor
public class IndexColumnResult {

    private String left;

    private String right;

    //判断对应的行，是否通过索引的筛选，默认为false，即使判断失误，最终也只是多一次查询，而不是漏掉
    private boolean ifMark = false;

    public IndexColumnResult(boolean ifMark) {
        this.ifMark = ifMark;
    }
    public IndexColumnResult(String left,String right) {
        this.left = left;
        this.right =right;
        this.ifMark = false;
    }

}
