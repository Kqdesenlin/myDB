package com.domain.event;

import com.Infrastructure.Service.TypeConverUtils;
import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.ColumnValueInfo;
import com.Infrastructure.TableInfo.SelectItemInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.ExpressionVisitorWithBool;
import com.Infrastructure.Visitor.ExpressionVisitorWithRtn;
import com.Infrastructure.Visitor.FinalParserClass;
import com.Infrastructure.Visitor.SelectVisitor.SelectItemVisitorWithRtn;
import com.domain.Entity.DeleteEntity;
import com.domain.Entity.InsertEntity;
import com.domain.Entity.SelectEntity;
import com.domain.Entity.UpdateEntity;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.Entity.result.SelectResult;
import com.domain.repository.TableConstant;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DMLOperate {

    private CheckOperate checkOperate = new CheckOperate();

    private static Logger logger = Logger.getLogger("log_dmlOperate");

    public OperateResult select(SelectEntity selectEntity) {
        //获取表
        TableInfo tempTableInfo = selectEntity.getTableInfo();
        List<ColumnInfo> columnInfoList = tempTableInfo.getColumnInfoList();
        //expression解析
        List<String> columnOrder = tempTableInfo.getRulesOrder();
        BTree<Integer, List<String>> bTree = tempTableInfo.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
        Expression expression = selectEntity.getWhereExpression();
        List<Integer> whereFilterPK = new ArrayList<>();
        if (null != expression) {
            //获取每一行
            while (iterator.hasNext()) {
                Entry<Integer, List<String>> entry = iterator.next();
                List<String> columnValue = entry.getValue();
                ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnOrder, columnValue);
                //构建行对象，然后注入到visitor中
                ExpressionVisitorWithBool expressionVisitorWithBool = new ExpressionVisitorWithBool(columnValueInfo);
                expression.accept(expressionVisitorWithBool);
                //把未通过的，不符合的添加到list
                if (!expressionVisitorWithBool.isIfPass()) {
                    whereFilterPK.add(entry.getKey());
                }
            }
        }

        //select ...from 之间，对最后得到的值进行筛选
        List<SelectItem> selectItemList = selectEntity.getSelectItemList();
        List<SelectItemInfo> selectItemInfoList = new ArrayList<>();
        for (int var1 = 0; var1 < selectItemList.size(); var1++) {
            SelectItem selectItem = selectItemList.get(var1);
            //需要返回一个list，list内是多条item，例如,item可能是对应的表中的值，也可能是常量
            SelectItemVisitorWithRtn selectItemVisitorWithRtn = new SelectItemVisitorWithRtn();
            selectItemVisitorWithRtn.setColumnOrder(columnOrder);
            selectItemVisitorWithRtn.setColumnInfoList(columnInfoList);
            selectItem.accept(selectItemVisitorWithRtn);
            selectItemInfoList.addAll(selectItemVisitorWithRtn.getSelectItemInfoList());
        }
        //对每一行的数据，根据selectItem的解析需求，获取值，最终添加到返回请求中
        List<List<String>> finalSelectResult = new ArrayList<>();
        Iterator<Entry<Integer, List<String>>> getIterator = bTree.iterator();
        while (getIterator.hasNext()) {
            Entry<Integer, List<String>> entry = getIterator.next();
            int key = entry.getKey();
            if (whereFilterPK.contains(key)){
                continue;
            }
            List<String> value = entry.getValue();
            List<String> newValue = new ArrayList<>();
            for (int var1 = 0; var1 < selectItemInfoList.size(); var1++) {
                SelectItemInfo selectItemInfo = selectItemInfoList.get(var1);
                if (selectItemInfo.isIfConstant()) {
                    newValue.add(selectItemInfo.getConstant());
                } else {
                    newValue.add(value.get(selectItemInfo.getIndex()));
                }
            }
            finalSelectResult.add(newValue);
        }
        List<String> finalSelectColumn = selectItemInfoList.stream().map(SelectItemInfo::getItemName).collect(Collectors.toList());

        return OperateResult.selectOk("查询成功", new SelectResult(finalSelectColumn, finalSelectResult));

    }
//    public SelectResult selectTotalTable(SelectEntity selectEntity){
//        String tableName = selectEntity.getTableName();
//        if (!checkOperate.ifTableExists(tableName)){
//            return SelectResult.error("查找表不存在");
//        }
//        //获取表
//        TableInfo tableInfo = TableConstant.getTableByName(tableName);
//        List<String> tableRules = tableInfo.getRulesOrder();
//        //全表扫描
//        List<Entry<Integer,List<String>>> items = tableInfo.getBTree().breathFirstSearch();
//        long itemNumber = items.size();
//        //where表达式筛选
//        logger.info("beforeFilter:" + items.toString());
//        //selectItems筛选
//        List<String> selectItems = selectEntity.getSelectItems();
//        items.forEach(entry -> {
//            List<String> entryValue = entry.getValue();
//            List<String> afterFilterItems = new ArrayList<>();
//            for (String selectItem : selectItems) {
//                for (int var2 = 0; var2 < tableRules.size(); var2++) {
//                    if (selectItem.equals(tableRules.get(var2))) {
//                        afterFilterItems.add(entryValue.get(var2));
//                        break;
//                    }
//                }
//            }
//            entry.setValue(afterFilterItems);
//        });
//        logger.info("afterFilter:" + items.toString());
//        List<List<String>> filtedItems;
//        //有主键选项，返回主键
//        if (selectItems.contains(TableConstant.primaryKey)){
//            filtedItems = (List<List<String>>)items.stream()
//                    .sorted(new EntityComparator())
//                    .map(entry -> {
//                        List<String> entryToList = new ArrayList<>();
//                        entryToList.add(String.valueOf(entry.getKey()));
//                        entryToList.addAll(entry.getValue());
//                        return entryToList;
//                    }).collect(Collectors.toList());
//        } else {
//            //无主键选项，不添加主键
//            filtedItems = (List<List<String>>)items.stream()
//                    .sorted(new EntityComparator())
//                    .map(entry -> {
//                        return (List<String>) new ArrayList<String>(entry.getValue());
//                    }).collect(Collectors.toList());
//        }
//
//        return SelectResult.ok("查询成功").setRules(selectItems).setItems(filtedItems);
//    }
//
//    /**
//     * 带条件的复杂查询
//     * @param complexSelectEntity
//     * @return
//     */
//    public SelectResult complexSelectTotalTable(ComplexSelectEntity complexSelectEntity){
//        String tableName = complexSelectEntity.getTableName();
//        if (!checkOperate.ifTableExists(tableName)){
//            return SelectResult.error("查找表不存在");
//        }
//        TableInfo tableInfo = TableConstant.getTableByName(tableName);
//
//        return null;
//    }


    public OperateResult insert(InsertEntity insertEntity) {
        OperateResult operateResult = OperateResult.ok("插入成功");
        String tableName = insertEntity.getTableName();
        if (!checkOperate.ifTableExists(tableName)) {
            return OperateResult.error("插入表不存在");
        }

        operateResult = checkOperate.ifInsertItemsLegal(insertEntity);
        if (ResultCode.ok.getResultCode()
                != operateResult.getCode().getResultCode()) {
            return operateResult;
        }

        TableInfo tableInfo = TableConstant.getTableByName(tableName);
        List<String> columnOrder = tableInfo.getRulesOrder();
        if (null == insertEntity.getColumnOrder()) {
            insertEntity.setColumnOrder(columnOrder);
        }
        List<String> insertItems = TypeConverUtils.mapToListByListOrder(
                insertEntity, tableInfo.getRulesOrder());
        Integer primaryKey = tableInfo.primaryKey.getAndIncrement();
        Entry<Integer, List<String>> insertEntry = new Entry<Integer, List<String>>(primaryKey, insertItems);
        tableInfo.getBTree().addNode(insertEntry);
        return OperateResult.ok("插入成功");
    }

    public OperateResult delete(DeleteEntity deleteEntity) {
        TableInfo tempTableInfo = deleteEntity.getTableInfo();
        //expression解析
        List<String> columnOrder = tempTableInfo.getRulesOrder();
        BTree<Integer, List<String>> bTree = tempTableInfo.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
        Expression expression = deleteEntity.getExpression();
        if (null != expression) {
            List<Integer> whereFilterPK = new ArrayList<>();
            //获取每一行
            while (iterator.hasNext()) {
                Entry<Integer, List<String>> entry = iterator.next();
                List<String> columnValue = entry.getValue();
                ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnOrder, columnValue);
                //构建行对象，然后注入到visitor中
                ExpressionVisitorWithBool expressionVisitorWithBool = new ExpressionVisitorWithBool(columnValueInfo);
                expression.accept(expressionVisitorWithBool);
                //把未通过的，不符合的添加到list
                if (expressionVisitorWithBool.isIfPass()) {
                    whereFilterPK.add(entry.getKey());
                }
            }
            //删除不符合条件的值
            for (Integer filterPK : whereFilterPK) {
                bTree.delete(filterPK);
            }
        } else {
            bTree = new BTree<>();
        }
        return OperateResult.ok("删除成功");
    }


    /**
     * 更新操作细节
     * 当同时更新多个属性的时候，例如update table1 column1 = 1,column2 = column1
     * 此时，更新顺序从左到右，如果右边读取到了左边的属性，按照修改后的读取，
     * 如果左边读取到了右边更新的数据，按照表中原来的数据读取
     * @param updateEntity
     * @return
     */
    public OperateResult update(UpdateEntity updateEntity) {
        TableInfo tempTableInfo = updateEntity.getTableInfo();
        List<String> columnOrder = tempTableInfo.getRulesOrder();
        List<String> columnUpdate = updateEntity.getColumnList();
        List<Expression> expressionList = updateEntity.getExpressionList();
        BTree<Integer,List<String>> bTree = tempTableInfo.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
        Expression whereExp = updateEntity.getExpression();

        //对where解析
        while (iterator.hasNext()) {
            Entry<Integer,List<String>> entry = iterator.next();
            List<String> columnValue = entry.getValue();
            //创建集合类，方便修改
            ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnOrder, columnValue);
            //构建行对象，然后注入到visitor中
            ExpressionVisitorWithBool expressionVisitorWithBool = new ExpressionVisitorWithBool(columnValueInfo);
            whereExp.accept(expressionVisitorWithBool);
            //如果通过判断，则进行update修改
            if (expressionVisitorWithBool.isIfPass()) {
                for (int var1 = 0; var1<expressionList.size();++var1) {
                    String updateColumn = columnUpdate.get(var1);
                    String updateValue = "";
                    //第一种情况，如果是一个常量或者已知量，则直接获取,不然需要解析获取
                    Expression tempExp = expressionList.get(var1);
                    if (FinalParserClass.ifConstant(tempExp)) {
                        updateValue = tempExp.toString();
                    } else if (FinalParserClass.ifColumn(tempExp)) {
                        updateValue = columnValueInfo.findValueByName(tempExp.toString());
                    } else {
                        ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
                        tempExp.accept(visitorWithRtn);
                        updateValue = visitorWithRtn.getRtn();
                    }
                    columnValueInfo.updateValueByName(updateColumn,updateValue);
                }
                //把最终解析完的值替换掉
                entry.setValue(columnValueInfo.getColumnValueList());
            }
        }
        return OperateResult.ok("修改成功");
    }
}
