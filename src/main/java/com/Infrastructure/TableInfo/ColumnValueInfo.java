package com.Infrastructure.TableInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 在过滤筛选行的过程中的每一行数据的存储
 */
@Data
@AllArgsConstructor
public class ColumnValueInfo {

    //列名
    private List<String> columnNameList;
    //对应的值
    private List<String> columnValueList;
}
