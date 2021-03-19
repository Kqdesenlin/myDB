package com.domain.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class InsertEntity {
    String tableName;
    /**
     * 遵循列名在前，具体值在后
     */
    Map<String,String> items;
}
