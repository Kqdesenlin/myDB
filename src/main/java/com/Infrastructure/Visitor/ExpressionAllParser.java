package com.Infrastructure.Visitor;

import com.Infrastructure.Visitor.FinalExpression.FinalBinaryExpression;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:对expression完全解析
 */
public class ExpressionAllParser implements ExpressionVisitor {

    private List<FinalBinaryExpression> finalBinaryExpressionList = new ArrayList<>();
    //解析到是一个括号(插入语)
    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {

    }

    @Override
    public void visit(SubSelect subSelect) {

    }
}
