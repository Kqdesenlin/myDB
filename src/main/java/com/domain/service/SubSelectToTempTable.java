package com.domain.service;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.ColumnValueInfo;
import com.Infrastructure.TableInfo.SelectItemInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.ExpressionVisitorWithBool;
import com.Infrastructure.Visitor.FinalExpression.SelectItemVisitorWithRtn;
import com.Infrastructure.Visitor.FromItemVisitorWithRtn;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 将from中的子查询，转换成一张临时表
 */
public class SubSelectToTempTable {

    private static final Logger logger = Logger.getLogger("log_" + SubSelectToTempTable.class.getSimpleName());

    public static TableInfo selectBodyToTempTable(SelectBody selectBody) {

        PlainSelect plainSelect = (PlainSelect) selectBody;
        //from解析
        FromItemVisitorWithRtn visitorWithRtn = new FromItemVisitorWithRtn();
        FromItem fromItem = plainSelect.getFromItem();
        fromItem.accept(visitorWithRtn);
        //！！！！需要复制表，即使可以直接在tableconstant中获取，也要复制一张
        TableInfo tempTableInfo = visitorWithRtn.getTableInfo();
        if (null == tempTableInfo) {
            return null;
        }
        //expression解析
        List<String> columnName = tempTableInfo.getRulesOrder();
        BTree<Integer,List<String>> bTree = tempTableInfo.getBTree();
        Iterator<Entry<Integer,List<String>>> iterator = bTree.iterator();
        //获取select的where部分的Expression
        Expression expression = plainSelect.getWhere();
        List<Integer> whereFilterPK = new ArrayList<>();
        //获取每一行
        while (iterator.hasNext()) {
            Entry<Integer,List<String>> entry = iterator.next();
            List<String> columnValue = entry.getValue();
            ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnName,columnValue);
            //构建行对象，然后注入到visitor中
            ExpressionVisitorWithBool expressionVisitorWithBool = new ExpressionVisitorWithBool(columnValueInfo);
            expression.accept(expressionVisitorWithBool);
            //如果通过where判断，把主键添加到list
            if (expressionVisitorWithBool.isIfPass()) {
                whereFilterPK.add(entry.getKey());
            }
        }
        //删除不符合条件的值
        for (Integer filterPK : whereFilterPK) {
            bTree.delete(filterPK);
        }

        //select ...from 之间，对最后得到的值进行筛选
        List<SelectItem> selectItemList = plainSelect.getSelectItems();
        List<String> columnOrder = tempTableInfo.getRulesOrder();
        List<SelectItemInfo> selectItemInfoList = new ArrayList<>();
        for (int var1 = 0; var1 < selectItemList.size(); var1++) {
            SelectItem selectItem = selectItemList.get(var1);
            //需要返回一个list，list内是多条item，例如,item可能是对应的表中的值，也可能是常量
            SelectItemVisitorWithRtn selectItemVisitorWithRtn = new SelectItemVisitorWithRtn();
            selectItemVisitorWithRtn.setColumnOrder(columnOrder);
//            selectItemVisitorWithRtn.setColumnInfoList();
            selectItem.accept(selectItemVisitorWithRtn);
            selectItemInfoList.addAll(selectItemVisitorWithRtn.getSelectItemInfoList());
        }

        //对每一行的数据，根据selectItem的解析需求，替换值
        Iterator<Entry<Integer,List<String>>> replaceIterator = bTree.iterator();
        while (replaceIterator.hasNext()) {
            Entry<Integer,List<String>> entry = iterator.next();
            List<String> value = entry.getValue();
            List<String> replaceValue = new ArrayList<>();
            for (int var1 = 0;var1<selectItemInfoList.size();var1++) {
                SelectItemInfo selectItemInfo = selectItemInfoList.get(var1);
                if (selectItemInfo.isIfConstant()) {
                    replaceValue.add(selectItemInfo.getConstant());
                } else {
                    replaceValue.add(value.get(selectItemInfo.getIndex()));
                }
            }
            entry.setValue(replaceValue);
        }

        //替换除了btree之外的别的属性
        List<ColumnInfo> newColumnInfoList = selectItemInfoList.stream()
                .map(selectItemInfo ->
                    new ColumnInfo().setColumnName(selectItemInfo.getItemName())
                            .setColumnType(selectItemInfo.getColumnType().getColumnType())
                            .setColumnArgument(selectItemInfo.getColumnType().getColumnArgument())
                            .setNotNull(selectItemInfo.getColumnType().isNotNull())
                            .setUnique(selectItemInfo.getColumnType().isUnique())
                            .setPrimaryKey(selectItemInfo.getColumnType().isPrimaryKey()))
                .collect(Collectors.toList());
        tempTableInfo.setColumnInfoList(newColumnInfoList);
        tempTableInfo.setRulesOrder(selectItemInfoList.stream().map(SelectItemInfo::getItemName).collect(Collectors.toList());
        return tempTableInfo;
    }

    public static TableInfo subSelectToTempTable(SubSelect subSelect) {
        //判断内部是一个select，还是多个平行的select
        SelectBody selectBody = subSelect.getSelectBody();
        //内部只有一个select
        if(selectBody instanceof PlainSelect) {
            //跳转到单一表的select
            TableInfo tableInfo = selectBodyToTempTable(selectBody);
            return tableInfo;
        }
        //对内部有多个select
        if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            //获取多个子查询
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            //获取多个子查询之间的关联符号（union ,union all）
            List<SetOperation> operationList = setOperationList.getOperations();
            List<TableInfo> tempSubSelectTableList = new ArrayList<>();
            //对多个select进行解析，都添加到TableList中
            for (SelectBody tempSelect : selectBodyList) {
                TableInfo tableInfo = selectBodyToTempTable(tempSelect);
                tempSubSelectTableList.add(tableInfo);
            }
            TableInfo nextTable = tempSubSelectTableList.get(0);
            for (int i = 0;i< operationList.size();i++) {
                SetOperation operation = operationList.get(i);
                if (operation instanceof UnionOp) {
                    nextTable = TableTrans.twoTableTransToTable(nextTable,tempSubSelectTableList.get(i+1));
                }
                if (operation instanceof ExceptOp) {
                    logger.info("fromItem meet exceptOp");
                }
                if (operation instanceof MinusOp) {
                    logger.info("fromItem meet minusOp");
                }
                if (operation instanceof IntersectOp) {
                    logger.info("fromItem meet intersectOp");
                }
            }
            return nextTable;
        }
        return null;
    }
}
