package com.Infrastructure.TableInfo;

import com.domain.Entity.enums.ColumnSpecsEnums;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: zhangQY
 * @date: 2021/3/22
 * @description:列信息
 */
@Data
@AllArgsConstructor
public class ColumnInfo {
    private String columnName;
    private String columnType;
    private int columnArgument;
    private boolean notNull;
    private boolean unique;
    private boolean primaryKey;

    public ColumnInfo() {

    }
    public ColumnInfo(String name ,String type) {
        this.columnName = name;
        this.columnType = type;
        this.columnArgument = -1;
        this.notNull = false;
        this.unique = false;
        this.primaryKey = false;
    }

    public ColumnInfo(String name,String type,int columnArgument) {
        this.columnName = name;
        this.columnType = type;
        this.columnArgument = columnArgument;
        this.notNull = false;
        this.unique = false;
        this.primaryKey = false;
    }

    public void addSpecs(ColumnSpecsEnums columnSpecsEnums) {
        switch (columnSpecsEnums){
            case not_null:
                this.notNull = true;
                break;
            case unique:
                this.unique = true;
                break;
            case primary_key:
                this.primaryKey = true;
                break;
        }
    }
}
