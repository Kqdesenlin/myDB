package com.domain.event;

import com.Infrastructure.IndexInfo.IndexInfo;
import com.Infrastructure.TableInfo.ColumnValueInfo;
import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.ExpressionVisitorWithBool;
import com.Infrastructure.Visitor.IndexVisitor.IndexExpressionVisitorWithBool;
import com.domain.Entity.bTree.BTree;
import com.domain.Entity.bTree.Entry;
import net.sf.jsqlparser.expression.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/13
 * @description:
 */
public class WhereOperate {

    public static TableInfo where(TableInfo table, Expression expression) {
        //expression解析
        List<String> columnOrder = table.getRulesOrder();
        BTree<Integer, List<String>> bTree = table.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
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
            for (Integer filterPK : whereFilterPK) {
                bTree.delete(filterPK);
            }
            return table;
        }
        return table;
    }

    public static TableInfo whereFiterPK(TableInfo tableInfo, Expression expression,List<Integer> pkList) {
        List<String> columnOrder = tableInfo.getRulesOrder();
        BTree<Integer, List<String>> bTree = tableInfo.getBTree();
        Iterator<Entry<Integer, List<String>>> iterator = bTree.iterator();
        List<Integer> whereFilterPK = new ArrayList<>();
        if (null != expression) {
            //获取每一行
            while (iterator.hasNext()) {
                Entry<Integer, List<String>> entry = iterator.next();
                List<String> columnValue = entry.getValue();
                Integer key = entry.getKey();
                if (!pkList.contains(key)) {
                    whereFilterPK.add(key);
                    continue;
                }
                ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnOrder, columnValue);
                //构建行对象，然后注入到visitor中
                ExpressionVisitorWithBool expressionVisitorWithBool = new ExpressionVisitorWithBool(columnValueInfo);
                expression.accept(expressionVisitorWithBool);
                //把未通过的，不符合的添加到list
                if (!expressionVisitorWithBool.isIfPass()) {
                    whereFilterPK.add(entry.getKey());
                }
            }
            for (Integer filterPK : whereFilterPK) {
                bTree.delete(filterPK);
            }
            return tableInfo;
        }
        return tableInfo;
    }
    public static List<Integer> indexWhere(IndexInfo indexInfo,Expression expression) {
        List<Integer> list = new ArrayList<>();
        List<String> columnOrder = indexInfo.getRulesOrder();
        BTree<List<String>,Integer> bTree = indexInfo.getBTree();
        Iterator<Entry<List<String>,Integer>> iterator = bTree.iterator();
        while (iterator.hasNext()) {
            Entry<List<String>,Integer> entry = iterator.next();
            List<String> columnValue = entry.getKey();
            ColumnValueInfo columnValueInfo = new ColumnValueInfo(columnOrder,columnValue);
            IndexExpressionVisitorWithBool expressionVisitorWithBool = new IndexExpressionVisitorWithBool(columnValueInfo);
            expression.accept(expressionVisitorWithBool);
            if (expressionVisitorWithBool.isIfPass()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }
}
