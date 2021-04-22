package com.Infrastructure.Visitor;

import com.Infrastructure.TableInfo.TableInfo;
import com.domain.Entity.result.OperateResult;
import com.domain.repository.TableConstant;
import com.domain.service.SubSelectToTempTable;
import lombok.Getter;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @author: zhangQY
 * @date: 2021/3/26
 * @description: 返回临时表
 */
public class FromItemVisitorWithRtn implements FromItemVisitor {

    //作为解析之后的返回
    private TableInfo tableInfo;

    @Getter
    private OperateResult errorResult;

    public TableInfo getTableInfo() {
        return this.tableInfo;
    }

    @Override
    public void visit(Table tableName) {
        TableInfo tableInfo = TableConstant.getTableByName(tableName.getName());

        try {
            this.tableInfo = (TableInfo) BeanUtils.cloneBean(tableInfo);
        } catch (Exception e) {
            errorResult = OperateResult.error(tableName.getName()+"对应表不存在，或者复制失败",e.getMessage());
        }
    }

    @Override
    public void visit(SubSelect subSelect){
        TableInfo tableInfo = SubSelectToTempTable.subSelectToTempTable(subSelect);
        try {
            TableInfo newTableInfo = (TableInfo) BeanUtils.cloneBean(tableInfo);
            this.tableInfo = newTableInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
