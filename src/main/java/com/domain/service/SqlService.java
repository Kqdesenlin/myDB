package com.domain.service;

import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.FromItemVisitorWithRtn;
import com.Infrastructure.Visitor.SelectVisitor.ItemsListVisitorWithList;
import com.domain.Entity.DeleteEntity;
import com.domain.Entity.InsertEntity;
import com.domain.Entity.SelectEntity;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.common.TableInfoEntity;
import com.domain.Entity.createTable.CreateTableEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.event.DDLOperate;
import com.domain.event.DMLOperate;
import com.domain.repository.TableConstant;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:将sql的解析结果映射到domain的具体操作
 */
public class SqlService {

    Logger logger = Logger.getLogger("log_" + SqlService.class.getSimpleName());

    DDLOperate ddlOperate = new DDLOperate();

    DMLOperate dmlOperate = new DMLOperate();

    public OperateResult sqlMapToDML(String sql) throws JSQLParserException {
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
            return sqlMapToDelete((Delete) statement);
        }
        if (statement instanceof CreateTable) {
            return sqlMapToCreate((CreateTable) statement);
        }
        return null;

    }

    public OperateResult sqlMapToCreate(CreateTable createTable) {
        /*
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
            String argument = null;
            if (null!=colDataType.getArgumentsStringList()){
                argument = colDataType.getArgumentsStringList().get(0);
            }
            columnInfoEntity.setColumnArguments(argument);
            //设置特殊参数
            if (columnDefinition.getColumnSpecs().size()>0) {
                columnInfoEntity.setColumnSpecs(columnDefinition.getColumnSpecs()
                        .stream().map(String::toUpperCase).collect(Collectors.toList()));
            }
            createTableEntity.getColumnInfo().add(columnInfoEntity);
        }
        /*
         * 执行操作
         */
        OperateResult operateResult = ddlOperate.createTable(createTableEntity);
        return operateResult;
    }

    public OperateResult sqlMapToInsert(Insert insert) {
        InsertEntity insertEntity = new InsertEntity();
        //添加表名
        String table = insert.getTable().getName();
        insertEntity.setTableName(table);
        //添加列名
        if (null != insert.getColumns()) {
            List<String> columnOrder = insert.getColumns().stream()
                    .map(Column::getColumnName)
                    .collect(Collectors.toList());
            insertEntity.setColumnOrder(columnOrder);
        } else {
            //如果没有列名，在insert的时候，从tableInfo中获取
        }
        //添加具体插入的值
        List<String> columnList = new ArrayList<>();
        ItemsList itemsList = insert.getItemsList();
        ItemsListVisitorWithList visitorWithList = new ItemsListVisitorWithList();
        visitorWithList.setColumnList(columnList);
        itemsList.accept(visitorWithList);
        insertEntity.setColumnValue(visitorWithList.getColumnList());
        OperateResult operateResult = dmlOperate.insert(insertEntity);
        return operateResult;
    }

    public OperateResult sqlMapToDelete(Delete delete) {
        DeleteEntity deleteEntity = new DeleteEntity();
        //from ... where
        TableInfo tempTableInfo = TableConstant.getTableByName(delete.getTable().getName());
        deleteEntity.setTableInfo(tempTableInfo);
        deleteEntity.setExpression(delete.getWhere());
        OperateResult operateResult = dmlOperate.delete(deleteEntity);
        return operateResult;
    }

    public OperateResult sqlMapToUpdate() {
        return null;
    }

    public OperateResult sqlMapToSelect(Select select){

        //fromItem添加
        SelectEntity selectEntity = new SelectEntity();
        PlainSelect plainSelect = (PlainSelect)select.getSelectBody();
        FromItem fromItem = plainSelect.getFromItem();
        FromItemVisitorWithRtn visitorWithRtn = new FromItemVisitorWithRtn();
        fromItem.accept(visitorWithRtn);
        TableInfo tempTableInfo = visitorWithRtn.getTableInfo();
        selectEntity.setTableInfo(tempTableInfo);
        //whereExpression添加
        Expression whereExpression = plainSelect.getWhere();
        selectEntity.setWhereExpression(whereExpression);
        //ItemList添加
        List<SelectItem> selectItemList = plainSelect.getSelectItems();
        selectEntity.setSelectItemList(selectItemList);
        return dmlOperate.select(selectEntity);
    }

}
