package com.domain;

import com.BTree.BTree;
import com.BTree.Entry;
import com.Constant.TableConstant;
import com.Infrastructure.Entity.*;
import com.Infrastructure.Service.TypeConverUtils;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Utils.EntityComparator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DMLOperate {

    private CheckOperate checkOperate = new CheckOperate();

    private static Logger logger = Logger.getLogger("log_dmlOperate");

    public SelectResult selectTotalTable(SelectEntity selectEntity){
        String tableName = selectEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return SelectResult.error("查找表不存在");
        }
        //获取表
        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        List<String> tableRules = tableInfo.getRulesOrder();
        //全表扫描
        List<Entry<Integer,List<String>>> items = tableInfo.getBTree().breathFirstSearch();
        long itemNumber = items.size();
        //where表达式筛选
        logger.info("beforeFilter:" + items.toString());
        //selectItems筛选
        List<String> selectItems = selectEntity.getSelectItems();
        items.forEach(entry -> {
            List<String> entryValue = entry.getValue();
            List<String> afterFilterItems = new ArrayList<>();
            for (String selectItem : selectItems) {
                for (int var2 = 0; var2 < tableRules.size(); var2++) {
                    if (selectItem.equals(tableRules.get(var2))) {
                        afterFilterItems.add(entryValue.get(var2));
                        break;
                    }
                }
            }
            entry.setValue(afterFilterItems);
        });
        logger.info("afterFilter:" + items.toString());
        List<List<String>> filtedItems;
        //有主键选项，返回主键
        if (selectItems.contains(TableConstant.primaryKey)){
            filtedItems = (List<List<String>>)items.stream()
                    .sorted(new EntityComparator())
                    .map(entry -> {
                        List<String> entryToList = new ArrayList<>();
                        entryToList.add(String.valueOf(entry.getKey()));
                        entryToList.addAll(entry.getValue());
                        return entryToList;
                    }).collect(Collectors.toList());
        } else {
            //无主键选项，不添加主键
            filtedItems = (List<List<String>>)items.stream()
                    .sorted(new EntityComparator())
                    .map(entry -> {
                        return (List<String>) new ArrayList<String>(entry.getValue());
                    }).collect(Collectors.toList());
        }

        return SelectResult.ok("查询成功").setRules(selectItems).setItems(filtedItems);
    }

    /**
     * 带条件的复杂查询
     * @param complexSelectEntity
     * @return
     */
    public SelectResult complexSelectTotalTable(ComplexSelectEntity complexSelectEntity){
        String tableName = complexSelectEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return SelectResult.error("查找表不存在");
        }
        TableInfo tableInfo = TableConstant.getTableByName(tableName);

        return null;
    }


    public OperateResult insert(InsertEntity insertEntity) {
        OperateResult operateResult = OperateResult.ok("插入成功");
        String tableName = insertEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return OperateResult.error("插入表不存在");
        }

        operateResult = checkOperate.ifInsertItemsLegal(insertEntity);
        if(ResultCode.ok.getResultCode()
                != operateResult.getCode().getResultCode()){
            return operateResult;
        }

        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        List<String> insertItems = TypeConverUtils.mapToListByListOrder(
                insertEntity.getItems(),tableInfo.getRulesOrder());
        Integer primaryKey = tableInfo.primaryKey.getAndIncrement();
        Entry<Integer,List<String>> insertEntry = new Entry<Integer,List<String>>(primaryKey,insertItems);
        tableInfo.getBTree().addNode(insertEntry);
        return OperateResult.ok("插入成功");
    }

    public OperateResult delete(DeleteEntity deleteEntity){
        //有主键删除
        if (deleteEntity.isIfContainPK()){
            return deleteByPK(deleteEntity);
        }else{

        }
        return null;
        //无主键删除
    }

    public OperateResult deleteByPK(DeleteEntity deleteEntity){
        String tableName = deleteEntity.getTableName();
        Integer pk = Integer.valueOf(deleteEntity.getDeleteRules().get(TableConstant.primaryKey));
        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        Entry<Integer,List<String>> deleteEntry = tableInfo.getBTree().delete(pk);
        return checkOperate.checkDeleteResultLegal(deleteEntry);
    }

    public OperateResult deleteByRules(DeleteEntity deleteEntity){
        String tableName = deleteEntity.getTableName();
        return null;
    }
}
