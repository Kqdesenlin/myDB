package com.Infrastructure.TableInfo;

import lombok.Data;

/**
 * @author: zhangQY
 * @date: 2021/3/28
 * @description: select ... from之间的item
 */
@Data
public class SelectItemInfo {
    //最终用于显示的列名
    private String itemName;
    //判断是否是常量，还是表中的列
    private boolean ifConstant;
    //如果是列，则用列的位置，来表示
    private int index;
    //如果是常量，则表示对应的量
    private String constant;
    //column在原来的列中的类型
    private ColumnInfo columnType;
}
