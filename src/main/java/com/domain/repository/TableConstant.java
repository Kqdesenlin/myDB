package com.domain.repository;

import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.TableInfo.TempTableInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 具体表集合
 */

public class TableConstant {

    //实体表集合
    public static Map<String, TableInfo> tableMap = new HashMap<String, TableInfo>();

    //虚表，零时表集合
    public static Map<String, TempTableInfo> tempTableMap = new HashMap<>();

    public static final String primaryKey = "PRIMARYKEY";

    public static TableInfo getTableByName(String name){
        return tableMap.get(name);
    }

}
