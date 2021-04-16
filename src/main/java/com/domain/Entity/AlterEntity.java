package com.domain.Entity;

import com.domain.Entity.common.ColumnInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/15
 * @description:
 */
@Data
public class AlterEntity {

    private String table;

    private List<ColumnInfoEntity> addColumnList;

    private List<ColumnInfoEntity> alterColumnList;

    private List<ColumnInfoEntity> dropColumnList;
}
