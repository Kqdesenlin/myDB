package com.domain.service;

import com.domain.Entity.InsertEntity;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.createTable.CreateTableEntity;
import com.domain.Entity.common.TableInfoEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.event.DDLOperate;
import com.domain.event.sqlParser.SelectParser;
import com.interfaces.dto.ResultDto;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:将sql的解析结果映射到domain的具体操作
 */
public class SqlToEntity {

    Logger logger = Logger.getLogger("log_" + SqlToEntity.class.getSimpleName());

    SelectParser selectParser = new SelectParser();

    DDLOperate ddlOperate = new DDLOperate();

    public ResultDto sqlMapToDML(String sql) throws JSQLParserException {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        logger.info(sql);
        if (statement instanceof Select){
            return sqlMapToSelect((Select) statement);
        }
        if (statement instanceof Insert){
            return sqlMapToInsert((Insert) statement);
        }
        if (statement instanceof Update){
        }
        if (statement instanceof Delete){
        }
        if (statement instanceof CreateTable) {
            return sqlMapToCreate((CreateTable) statement);
        }
        return null;

    }

    public ResultDto sqlMapToCreate(CreateTable createTable) {
        /**
         * 装配entity
         */
        CreateTableEntity createTableEntity = new CreateTableEntity();
        //构建表信息
        TableInfoEntity tableInfoEntity = new TableInfoEntity();
        tableInfoEntity.setTableName(createTable.getTable().getName());
        //添加
        createTableEntity.setTableInfo(tableInfoEntity);
        //
        for (ColumnDefinition columnDefinition : createTable.getColumnDefinitions()) {
            ColumnInfoEntity columnInfoEntity = new ColumnInfoEntity();
            //设置列名称
            columnInfoEntity.setColumnName(columnDefinition.getColumnName());
            ColDataType colDataType = columnDefinition.getColDataType();
            //设置类型
            columnInfoEntity.setDataType(colDataType.getDataType());
            //设置类型参数
            columnInfoEntity.setColumnArguments(colDataType.getArgumentsStringList().get(0));
            //设置特殊参数
            if (columnDefinition.getColumnSpecs().size()>0) {
                columnInfoEntity.setColumnSpecs(columnDefinition.getColumnSpecs());
            }
            createTableEntity.getColumnInfo().add(columnInfoEntity);
        }
        /**
         * 执行操作
         */
        OperateResult operateResult = ddlOperate.createTable(createTableEntity);
        return null;
    }

    public ResultDto sqlMapToInsert(Insert insert) {
        InsertEntity insertEntity = new InsertEntity();
        //添加表名
        String table = insert.getTable().getName();
        insertEntity.setTableName(table);
        //添加列名
        List<String> columnOrder = insert.getColumns().stream()
                .map(Column::getColumnName)
                .collect(Collectors.toList());
        insertEntity.setColumnOrder(columnOrder);
        //添加具体插入的值
        insert.getItemsList().accept(
                new ItemsListVisitor() {
                    @Override
                    public void visit(ExpressionList expressionList) {

                    }

                    @Override
                    public void visit(SubSelect subSelect) {

                    }

                    @Override
                    public void visit(NamedExpressionList namedExpressionList) {

                    }

                    @Override
                    public void visit(MultiExpressionList multiExprList) {

                    }
                }
        );
        return null;
    }

    public ResultDto sqlMapToDelete() {
        return null;
    }

    public ResultDto sqlMapToUpdate() {
        return null;
    }

    public ResultDto sqlMapToSelect(Select select){
        return null;
    }

}
