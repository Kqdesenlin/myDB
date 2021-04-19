package com.domain.event;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.domain.Entity.AlterEntity;
import com.domain.Entity.CreateTempEntity;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.common.TableInfoEntity;
import com.domain.Entity.createTable.CreateTableEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.repository.TableConstant;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DDLOperate {

    private CheckOperate checkOperate = new CheckOperate();

    private DMLOperate dmlOperate = new DMLOperate();
    /**
     * 创建表
     * @param createTableEntity
     * @return
     */
    public  OperateResult createTable(CreateTableEntity createTableEntity){
        OperateResult rtn = OperateResult.ok("建表成功");
        TableInfoEntity tableInfoEntity = createTableEntity.getTableInfo();
        List<ColumnInfoEntity> columnInfoEntities = createTableEntity.getColumnInfo();
        String tableName = tableInfoEntity.getTableName();
        //判断是否已存在
        if (checkOperate.ifTableExists(tableName)){
            rtn = OperateResult.error("表已存在");
            return rtn;
        }
        //判断规则是否合法
        rtn = checkOperate.ifColumnLegal(columnInfoEntities);
        if (ResultCode.ok != rtn.getCode()){
            return rtn;
        }
        List<ColumnInfo> columnInfoList = (List<ColumnInfo>)rtn.getRtn();
        List<String> rulesOrder = columnInfoList.stream()
                .map(ColumnInfo::getColumnName)
                .collect(Collectors.toList());
        TableInfo newTable = new TableInfo(tableName,new BTree<Integer,List<String>>(),columnInfoList,rulesOrder);
        TableConstant.tableMap.put(tableName,newTable);
        rtn = OperateResult.ok("建表成功");
        return rtn;

    }

    public OperateResult createTempTable(CreateTempEntity createTempEntity){
//        List<String> tableList = createTempEntity.getTableList();
//        if (tableList.size()<2) {
//            return OperateResult.error("关联表少于2，无法建立临时表");
//        }
//        String driveTable = tableList.get(0);
//        String drivenTable = tableList.get(1);
//        TableInfo driveTableInfo = TableConstant.getTableByName(driveTable);
//        TableInfo drivenTableInfo = TableConstant.getTableByName(drivenTable);
//        //建立临时表
//        String newTableName = driveTable + drivenTable;
//        List<String> driveTableRuleOrder = driveTableInfo.getRulesOrder();
//        List<String> drivenTableRuleOrder = drivenTableInfo.getRulesOrder();
//        Map<String,String> driveTableRule = driveTableInfo.getRules();
//        Map<String,String> drivenTableRule = drivenTableInfo.getRules();
//        //如果两张表中存在相同名称的列，则各自替换为tableName_column
//        List<String> retainRule = new ArrayList<>(driveTableRuleOrder);
//        retainRule.retainAll(drivenTableRuleOrder);
//        List<String> newTableRuleOrder = new ArrayList<>();
//        //添加第一张表的列
//        for (int i = 0; i < driveTableRuleOrder.size(); i++){
//            String tempRule = driveTableRuleOrder.get(i);
//            if (retainRule.contains(tempRule)) {
//                newTableRuleOrder.add(driveTable + tempRule);
//            } else {
//                newTableRuleOrder.add(tempRule);
//            }
//        }
//        //添加第二张表的列
//        for (int j = 0; j < drivenTableRuleOrder.size(); j++){
//            String tempRule = drivenTableRuleOrder.get(j);
//            if (retainRule.contains(tempRule)) {
//                newTableRuleOrder.add(drivenTable + tempRule);
//            } else {
//                newTableRuleOrder.add(tempRule);
//            }
//        }
//        //添加第一张表列的约束规则
//        Map<String,String> newRules = new HashMap<>();
//        for (Map.Entry<String,String> entry : driveTableRule.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (retainRule.contains(key)) {
//                newRules.put(driveTable + key,value);
//            } else {
//                newRules.put(key,value);
//            }
//        }
//        //添加第二张表列的约束规则
//        for (Map.Entry<String,String> entry : drivenTableRule.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (retainRule.contains(key)) {
//                newRules.put(drivenTable + key,value);
//            } else {
//                newRules.put(key,value);
//            }
//        }
//        //根据约束规则和列的属性创建表
//        TempTableInfo tempTableInfo = new TempTableInfo(new BTree<Integer,List<String>>()
//                ,newRules,newTableRuleOrder,driveTable,drivenTable);
//        BTree<Integer,List<String>> newBTree = tempTableInfo.getBTree();
//        //添加到总表集
//        TableConstant.tempTableMap.put(newTableName,tempTableInfo);
//        BTree<Integer,List<String>> driveBTree = driveTableInfo.getBTree();
//        BTree<Integer,List<String>> drivenBTree = driveTableInfo.getBTree();
//        Iterator<Entry<Integer,List<String>>> driveIterator = driveBTree.iterator();
//        //两表做笛卡尔积，X * Y
//        while (driveIterator.hasNext()) {
//            List<String> driveTableEntryValue = driveIterator.next().getValue();
//            Iterator<Entry<Integer,List<String>>> drivenIterator = drivenBTree.iterator();
//            while (drivenIterator.hasNext()) {
//                List<String> drivenTableEntryValue = drivenIterator.next().getValue();
//                Entry<Integer,List<String>> entry = new Entry<>();
//                entry.setKey(tempTableInfo.getPrimaryKey().getAndIncrement());
//                List<String> newTableEntryValue = new ArrayList<>();
//                newTableEntryValue.addAll(driveTableEntryValue);
//                newTableEntryValue.addAll(drivenTableEntryValue);
//                entry.setValue(newTableEntryValue);
//                newBTree.addNode(entry);
//            }
//        }
//        return OperateResult.ok("临时表创建成功",newTableName);
        return null;
    }

    public OperateResult alterTable(AlterEntity alterEntity) {
        OperateResult rtn = OperateResult.ok("修改成功");
        String tableName = alterEntity.getTable();
        if (!checkOperate.ifTableExists(tableName)) {
            rtn = OperateResult.error("修改表不存在");
            return rtn;
        }
        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        rtn = checkOperate.ifAlterColumnLegal(tableInfo,alterEntity);
        if (ResultCode.ok != rtn.getCode()) {
            return rtn;
        }
        List<List<ColumnInfo>> addAlterDropList = (List<List<ColumnInfo>>)rtn.getRtn();
        List<String> tableOrder = tableInfo.getRulesOrder();
        List<ColumnInfo> oldTableColumnList = tableInfo.getColumnInfoList();
        BTree<Integer, List<String>> bTree = tableInfo.getBTree();

        List<ColumnInfo> addColumnList = addAlterDropList.get(0);
        List<ColumnInfo> alterColumnList = addAlterDropList.get(1);
        List<ColumnInfo> dropColumnList = addAlterDropList.get(2);
        //先删
        for (ColumnInfo dropColumn:dropColumnList) {
            String dropColumnName = dropColumn.getColumnName();
            int index = tableOrder.indexOf(dropColumnName);
            tableOrder.remove(index);
            oldTableColumnList.remove(index);
            Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
            while (iterator.hasNext()) {
                Entry<Integer, List<String>> entry = iterator.next();
                List<String> value = entry.getValue();
                value.remove(index);
            }
        }
        //再修改
        for (ColumnInfo alterColumn:alterColumnList) {
            String alterColumnName = alterColumn.getColumnName();
            int index = tableOrder.indexOf(alterColumnName);
            oldTableColumnList.set(index,alterColumn);
        }
        //再添加
        for (ColumnInfo addColumn:addColumnList) {
            tableOrder.add(addColumn.getColumnName());
            oldTableColumnList.add(addColumn);
            Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
            while (iterator.hasNext()) {
                Entry<Integer, List<String>> entry = iterator.next();
                List<String> value = entry.getValue();
                value.add("");
            }
        }
        return OperateResult.ok("修改成功");
    }


}
