package com.Infrastructure.Visitor;

import com.Infrastructure.TableInfo.TableInfo;
import com.domain.repository.TableConstant;
import com.domain.service.SubSelectToTempTable;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 返回临时表
 */
public class FromItemVisitorWithRtn implements FromItemVisitor {

    //作为解析之后的返回
    private TableInfo tableInfo;

    public TableInfo getTableInfo() {
        return this.tableInfo;
    }

    @Override
    public void visit(Table tableName) {
        TableInfo tableInfo = TableConstant.getTableByName(tableName.getName());
        this.tableInfo = tableInfo;
    }

    @Override
    public void visit(SubSelect subSelect) {
        TableInfo tableInfo = SubSelectToTempTable.subSelectToTempTable(subSelect);
        this.tableInfo = tableInfo;
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
