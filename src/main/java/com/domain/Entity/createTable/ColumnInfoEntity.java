package com.domain.Entity.createTable;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: zhangQY
 * @date: 2021/3/22
 * @description:列的具体信息
 */
@Data
public class ColumnInfoEntity {
    String dataType;
    String columnName;
    String columnArguments;
    //sql映射的entity只负责装配，不负责校验
    List<String> columnSpecs;
}
