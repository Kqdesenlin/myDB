package com.domain.service;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import com.domain.repository.TableConstant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 对表进行转化
 */
public class TableTrans {

    /**
     * 将两张表转换成一张新表
     * 出现的重复字段用表名_列名来替换
     * 全表链接，取笛卡尔积
     * @param driveTableInfo
     * @param drivenTableInfo
     * @return
     */
    public static TableInfo twoTableTransToTable(TableInfo driveTableInfo, TableInfo drivenTableInfo) {
        String driveTable = driveTableInfo.getTableName();
        String drivenTable = drivenTableInfo.getTableName();
        String newTableName = driveTable + drivenTable;
        List<String> driveTableRuleOrder = driveTableInfo.getRulesOrder();
        List<String> drivenTableRuleOrder = drivenTableInfo.getRulesOrder();
        List<ColumnInfo> driveTableInfoColumnList = driveTableInfo.getColumnInfoList();
        List<ColumnInfo> drivenTableInfoColumnInfoList = drivenTableInfo.getColumnInfoList();
        //如果两张表中存在相同名称的列，则各自替换为tableName_column
        List<String> retainRule = new ArrayList<>(driveTableRuleOrder);
        retainRule.retainAll(drivenTableRuleOrder);
        List<String> newTableRuleOrder = new ArrayList<>();
        //添加第一张表的列
        for (int i = 0; i < driveTableRuleOrder.size(); i++){
            String tempRule = driveTableRuleOrder.get(i);
            if (retainRule.contains(tempRule)) {
                newTableRuleOrder.add(driveTable + "." + tempRule);
            } else {
                newTableRuleOrder.add(tempRule);
            }
        }
        //添加第二张表的列
        for (int j = 0; j < drivenTableRuleOrder.size(); j++){
            String tempRule = drivenTableRuleOrder.get(j);
            if (retainRule.contains(tempRule)) {
                newTableRuleOrder.add(drivenTable + "." + tempRule);
            } else {
                newTableRuleOrder.add(tempRule);
            }
        }
        //添加第一张表列的约束规则
        List<ColumnInfo> newTableCOlumnInfo = new ArrayList<>();
        for (ColumnInfo columnInfo : driveTableInfoColumnList) {
            String columnName = columnInfo.getColumnName();
            //出现相同名字的替换成表名+列名
            if (retainRule.contains(columnName)) {

                ColumnInfo tempColumnInfo = new ColumnInfo()
                        .setColumnName(driveTable + "." + columnInfo.getColumnName())
                        .setColumnType(columnInfo.getColumnType())
                        .setColumnArgument(columnInfo.getColumnArgument())
                        .setNotNull(columnInfo.isNotNull())
                        .setUnique(columnInfo.isUnique())
                        .setPrimaryKey(columnInfo.isPrimaryKey());
                newTableCOlumnInfo.add(tempColumnInfo);
            } else {
                newTableCOlumnInfo.add(columnInfo);
            }
        }
        //添加第二张表列的约束规则
        for (ColumnInfo columnInfo : drivenTableInfoColumnInfoList) {
            String columnName = columnInfo.getColumnName();
            //出现相同名字的替换成表名+列名
            if (retainRule.contains(columnName)) {
                ColumnInfo tempColumnInfo = new ColumnInfo()
                        .setColumnName(driveTable + "." + columnInfo.getColumnName())
                        .setColumnType(columnInfo.getColumnType())
                        .setColumnArgument(columnInfo.getColumnArgument())
                        .setNotNull(columnInfo.isNotNull())
                        .setUnique(columnInfo.isUnique())
                        .setPrimaryKey(columnInfo.isPrimaryKey());
                newTableCOlumnInfo.add(tempColumnInfo);
            } else {
                newTableCOlumnInfo.add(columnInfo);
            }
        }
        //根据约束规则和列的属性创建表
        TableInfo tempTableInfo = new TableInfo(driveTable+drivenTable,new BTree<Integer,List<String>>()
                ,newTableCOlumnInfo,newTableRuleOrder);

        BTree<Integer,List<String>> newBTree = tempTableInfo.getBTree();
        //添加到总表集
        TableConstant.tableMap.put(newTableName,tempTableInfo);
        BTree<Integer,List<String>> driveBTree = driveTableInfo.getBTree();
        BTree<Integer,List<String>> drivenBTree = driveTableInfo.getBTree();
        Iterator<Entry<Integer,List<String>>> driveIterator = driveBTree.iterator();
        //两表做笛卡尔积，X * Y
        while (driveIterator.hasNext()) {
            List<String> driveTableEntryValue = driveIterator.next().getValue();
            Iterator<Entry<Integer,List<String>>> drivenIterator = drivenBTree.iterator();
            while (drivenIterator.hasNext()) {
                List<String> drivenTableEntryValue = drivenIterator.next().getValue();
                Entry<Integer,List<String>> entry = new Entry<>();
                entry.setKey(tempTableInfo.getPrimaryKey().getAndIncrement());
                List<String> newTableEntryValue = new ArrayList<>();
                newTableEntryValue.addAll(driveTableEntryValue);
                newTableEntryValue.addAll(drivenTableEntryValue);
                entry.setValue(newTableEntryValue);
                newBTree.addNode(entry);
            }
        }
        return tempTableInfo;
    }
}
