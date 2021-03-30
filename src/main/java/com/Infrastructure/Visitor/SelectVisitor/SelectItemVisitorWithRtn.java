package com.Infrastructure.Visitor.SelectVisitor;

import com.Infrastructure.TableInfo.ColumnInfo;
import com.Infrastructure.TableInfo.SelectItemInfo;
import com.Infrastructure.Visitor.FinalParserClass;
import com.domain.Entity.enums.ColumnTypeEnums;
import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/28
 * @description: 对selectItem进行解析，最终返回解析后的结果
 */
@Data
public class SelectItemVisitorWithRtn implements SelectItemVisitor {

    //用于返回的列集合
    List<SelectItemInfo> selectItemInfoList;

    //用于比对的表中的列集合
    List<String> columnOrder;

    //用于为每一列的返回值添加type
    List<ColumnInfo> columnInfoList;

    public SelectItemVisitorWithRtn() {
        this.selectItemInfoList = new ArrayList<>();
    }
    @Override
    public void visit(AllColumns allColumns) {

    }

    @Override
    public void visit(AllTableColumns allTableColumns) {

    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        Expression expression = selectExpressionItem.getExpression();
        if (FinalParserClass.ifCanFinalParser(expression)) {
            SelectItemInfo selectItemInfo = new SelectItemInfo();
            if (FinalParserClass.ifConstant(expression)) {
                selectItemInfo.setItemName(expression.toString());
                selectItemInfo.setConstant(expression.toString());
                selectItemInfo.setIfConstant(true);
                //addType
                selectItemInfo.setColumnType(
                        new ColumnInfo().setColumnType(ColumnTypeEnums.findTypeByExpression(expression))
                );
                selectItemInfoList.add(selectItemInfo);
            } else if (FinalParserClass.ifColumn(expression)) {
                selectItemInfo.setItemName(expression.toString());
                selectItemInfo.setIfConstant(false);
                for (int var1 = 0;var1 < columnOrder.size();var1++) {
                    if (columnOrder.get(var1).equals(expression.toString())) {
                        selectItemInfo.setIndex(var1);
                        selectItemInfo.setColumnType(columnInfoList.get(var1));
                    }
                }
                selectItemInfoList.add(selectItemInfo);
            }
        }
    }
}
