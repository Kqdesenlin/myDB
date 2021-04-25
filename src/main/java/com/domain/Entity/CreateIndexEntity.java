package com.domain.Entity;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.domain.Entity.enums.IndexTypeEnums;
import lombok.Data;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Data
public class CreateIndexEntity {

    String indexName;

    TableInfo tableInfo;

    IndexTypeEnums indexType;

    List<ColumnInfo> columnInfoList;
}
