package com.domain.Entity.createTable;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/22
 * @description: 创建表的实体集，在sql解析之后被包装
 */
@Data
public class CreateTableEntity {
    TableInfoEntity tableInfo;
    List<ColumnInfoEntity> columnInfo;

    public CreateTableEntity() {
        columnInfo = new ArrayList<>();
    }
}
