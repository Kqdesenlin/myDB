package com.Infrastructure.Visitor;

import com.Infrastructure.Visitor.FinalParserClass;
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
 * @date: 2021/3/25
 * @description:
 */
public class InsertParser implements ItemsListVisitor {
    List<String> columnValues = new ArrayList<>();
    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(ExpressionList expressionList) {
        List<Expression> expressions = expressionList.getExpressions();
        for (Expression expression : expressions) {
            if (FinalParserClass.ifCanFinalParser(expression)) {
                String value = expression.toString();
                columnValues.add(value);
            } else {
                ExpressionVisitorWithRtn expressionVisitorWithRtn = new ExpressionVisitorWithRtn();
                expression.accept(expressionVisitorWithRtn);
                columnValues.add(expressionVisitorWithRtn.getRtn());
            }
        }
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {

    }

    @Override
    public void visit(MultiExpressionList multiExprList) {

    }
}
