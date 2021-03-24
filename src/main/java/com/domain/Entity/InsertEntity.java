package com.domain.Entity;

import com.domain.Entity.common.TableInfoEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InsertEntity {

    String tableName;

    List<String> columnOrder;

    List<String> columnValue;

}
