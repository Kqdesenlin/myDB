package com.domain.Entity.middle;

import lombok.Data;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Data
public class CreateIndexMiddleEntity {

    String indexName;

    String indexType;

    String tableName;

    List<String> columnList;
}
