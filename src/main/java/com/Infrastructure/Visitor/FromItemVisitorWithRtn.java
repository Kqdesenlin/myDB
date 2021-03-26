package com.Infrastructure.Visitor;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 返回临时表
 */
public class FromItemVisitorWithRtn implements FromItemVisitor {

    @Override
    public void visit(Table tableName) {

    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(SubJoin subjoin) {
        //
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        //
    }

    @Override
    public void visit(ValuesList valuesList) {
        //
    }

    @Override
    public void visit(TableFunction tableFunction) {

    }

    @Override
    public void visit(ParenthesisFromItem aThis) {

    }
}
