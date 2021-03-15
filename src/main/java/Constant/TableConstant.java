package Constant;

import BTree.BTree;
import Infrastructure.TableInfo.TableInfo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 具体表集合
 */

public class TableConstant {
    public static Map<String, TableInfo> tableMap = new HashMap<String, TableInfo>();

    public static TableInfo getTableByName(String name){
        return tableMap.get(name);
    }
}
