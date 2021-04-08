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

    public String findValueByName(String name) {
        for (int var1 = 0; var1<columnNameList.size();++var1) {
            if (columnNameList.get(var1).equals(name)) {
                return columnValueList.get(var1);
            }
        }
        return "";
    }

    public void updateValueByName(String name,String value) {
        for (int var2 = 0;var2<columnNameList.size();++var2) {
            if (columnNameList.get(var2).equals(name)) {
                columnValueList.set(var2,value);
            }
        }
    }
}
