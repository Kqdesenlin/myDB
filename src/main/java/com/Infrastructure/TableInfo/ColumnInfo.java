package com.Infrastructure.TableInfo;

import com.domain.Entity.enums.ColumnSpecsEnums;
import lombok.AllArgsConstructor;

/**
 * @author: zhangQY
 * @date: 2021/3/22
 * @description: 列信息
 */
@AllArgsConstructor
public class ColumnInfo implements Cloneable{
    private String columnName;
    private String columnType;
    private int columnArgument;
    private boolean notNull;
    private boolean unique;
    private boolean primaryKey;

    public ColumnInfo() {

    }

    public ColumnInfo(String name , String type) {
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


    public ColumnInfo setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public ColumnInfo setColumnType(String columnType) {
        this.columnType = columnType;
        return this;
    }

    public ColumnInfo setColumnArgument(int columnArgument) {
        this.columnArgument = columnArgument;
        return this;
    }

    public ColumnInfo setNotNull(boolean notNull) {
        this.notNull = notNull;
        return this;
    }

    public ColumnInfo setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public ColumnInfo setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public int getColumnArgument() {
        return columnArgument;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
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
