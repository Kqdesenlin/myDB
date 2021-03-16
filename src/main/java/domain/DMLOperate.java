package domain;

import BTree.Entry;
import Constant.TableConstant;
import Infrastructure.Entity.*;
import Infrastructure.Service.TypeConverUtils;
import Infrastructure.TableInfo.TableInfo;
import Infrastructure.Utils.EntityComparator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DMLOperate {

    private CheckOperate checkOperate = new CheckOperate();


    public SelectResult selectSingleTable(SelectEntity selectEntity){
        String tableName = selectEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return SelectResult.error("查找表不存在");
        }
        //获取表
        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        //全表扫描
        List<Entry<Integer,List<String>>> items = tableInfo.getBTree().iterate();
        long itemNumber = items.size();
        //where表达式筛选

        //selectItems筛选
        List<String> selectItems = selectEntity.getSelectItems();
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


    public OperateResult insert(InsertEntity insertEntity) {
        OperateResult operateResult = OperateResult.ok("插入成功");
        String tableName = insertEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)){
            return OperateResult.error("插入表不存在");
        }

        operateResult = checkOperate.ifInsertItemsLegal(insertEntity);
        if(ResultCode.ok.getResultCode()
                != operateResult.code.getResultCode()){
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
}
