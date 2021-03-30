package com.Infrastructure.Visitor.SelectVisitor;

import lombok.Data;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/29
 * @description:
 */
@Data
public class ItemsListVisitorWithList implements ItemsListVisitor {

    private List<String> columnList;

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(ExpressionList expressionList) {
        List<Expression> expressionList1 = expressionList.getExpressions();
        for (Expression expression : expressionList1) {
            List<String> tempColumnList = new ArrayList<>();
            ItemVisitorWithList visitorWithList = new ItemVisitorWithList();
            visitorWithList.setColumnList(tempColumnList);
            expression.accept(visitorWithList);
            columnList.addAll(visitorWithList.getColumnList());
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {

    }

    @Override
    public void visit(MultiExpressionList multiExprList) {

    }
}
