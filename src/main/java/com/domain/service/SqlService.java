package com.domain.service;

import com.Infrastructure.TableInfo.TableInfo;
import com.Infrastructure.Visitor.FromItemVisitorWithRtn;
import com.Infrastructure.Visitor.SelectVisitor.ItemsListVisitorWithList;
import com.domain.Entity.*;
import com.domain.Entity.common.ColumnInfoEntity;
import com.domain.Entity.common.LimitPart;
import com.domain.Entity.common.TableInfoEntity;
import com.domain.Entity.createTable.CreateTableEntity;
import com.domain.Entity.middle.CreateIndexMiddleEntity;
import com.domain.Entity.result.DropEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.event.DDLOperate;
import com.domain.event.DMLOperate;
import com.domain.repository.TableConstant;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:将sql的解析结果映射到domain的具体操作
 */
@Slf4j
@Service
public class SqlService {

    DDLOperate ddlOperate = new DDLOperate();

    DMLOperate dmlOperate = new DMLOperate();

    public List<OperateResult> mutilSqlMapToState(String mutilSql) {
        Statements statements = null;
        List<OperateResult> resultList = new ArrayList<>();
        try {
            statements = CCJSqlParserUtil.parseStatements(mutilSql);
            List<Statement> statementList = statements.getStatements();
            for (int var1 = 0;var1 < statementList.size();++var1) {
                OperateResult result = stateMapToDML(statementList.get(var1));
                resultList.add(result);
                if (!ResultCode.ok.equals(result.getCode())&&!ResultCode.selectOk.equals(result.getCode())){
                    break;
                }
            }
        } catch (JSQLParserException e) {
            OperateResult errorResult = OperateResult.error("parser failed",e.getMessage());
            resultList.add(errorResult);
        }
        return resultList;
    }
    public OperateResult stateMapToDML(Statement statement) throws JSQLParserException {

        if (statement instanceof Select){
            return sqlMapToSelect((Select) statement);
        }
        if (statement instanceof Insert){
            return sqlMapToInsert((Insert) statement);
        }
        if (statement instanceof Update){
            return sqlMapToUpdate((Update) statement);
        }
        if (statement instanceof Delete){
            return sqlMapToDelete((Delete) statement);
        }
        if (statement instanceof CreateTable) {
            return sqlMapToCreate((CreateTable) statement);
        }
        if (statement instanceof Alter) {
            return sqlMapToAlter((Alter) statement);
        }
        if (statement instanceof Drop && ((Drop)statement).getType().equals("table")) {
            return sqlMapToDrop((Drop) statement);
        }
        if (statement instanceof CreateIndex) {
            return sqlMapToCreateIndex((CreateIndex) statement);
        }
        if (statement instanceof Drop &&((Drop)statement).getType().equals("index")) {
            return sqlMapToDropIndex((Drop)statement);
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
            if (null != columnDefinition.getColumnSpecs() && columnDefinition.getColumnSpecs().size()>0) {
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

    public OperateResult sqlMapToUpdate(Update update) {
        UpdateEntity entity = new UpdateEntity();
        //加表名
        TableInfo tempTableInfo = TableConstant.getTableByName(update.getTable().getName());
        entity.setTableInfo(tempTableInfo);
        //加列名
        List<String> columnList = update.getColumns().stream().
                map(Column::getColumnName).collect(Collectors.toList());
        entity.setColumnList(columnList);
        //加赋值
        List<Expression> expressionList = update.getExpressions();
        entity.setExpressionList(expressionList);
        //加where
        Expression expression = update.getWhere();
        entity.setExpression(expression);
        OperateResult result = dmlOperate.update(entity);
        return result;
    }

    public OperateResult sqlMapToSelect(Select select){

        //fromItem添加
        SelectEntity selectEntity = new SelectEntity();
        PlainSelect plainSelect = (PlainSelect)select.getSelectBody();
        FromItem fromItem = plainSelect.getFromItem();
        FromItemVisitorWithRtn visitorWithRtn = new FromItemVisitorWithRtn();
        fromItem.accept(visitorWithRtn);
        TableInfo tempTableInfo = visitorWithRtn.getTableInfo();
        if (null == tempTableInfo) {
            return visitorWithRtn.getErrorResult();
        }
        selectEntity.setTableInfo(tempTableInfo);
        //whereExpression添加
        Expression whereExpression = plainSelect.getWhere();
        selectEntity.setWhereExpression(whereExpression);
        //ItemList添加
        List<SelectItem> selectItemList = plainSelect.getSelectItems();
        selectEntity.setSelectItemList(selectItemList);

        //limit添加
        if (null != plainSelect.getLimit()) {
            Limit limit = plainSelect.getLimit();
            LimitPart limitPart = new LimitPart(limit.getOffset(),limit.getRowCount());
            selectEntity.setLimitPart(limitPart);
        }

        return dmlOperate.select(selectEntity);
    }

    public OperateResult sqlMapToAlter(Alter alter) {
        AlterEntity entity = new AlterEntity();
        entity.setTable(alter.getTable().getName());
        //初始化add,alter,droplist
        List<ColumnInfoEntity> addList = new ArrayList<>();
        List<ColumnInfoEntity> alterList = new ArrayList<>();
        List<ColumnInfoEntity> dropList = new ArrayList<>();
        for(AlterExpression expression : alter.getAlterExpressions()) {
            //获取对应的alter的类型
            String alterColumnType = expression.getOperation().name();
            if("ADD".equals(alterColumnType)) {
                AlterExpression.ColumnDataType columnDataType = expression.getColDataTypeList().get(0);
                ColumnInfoEntity tempColumn = new ColumnInfoEntity();
                //添加columnName
                tempColumn.setColumnName(columnDataType.getColumnName());

                //添加type
                tempColumn.setDataType(columnDataType.getColDataType().getDataType());
                List<String> sizes = columnDataType.getColDataType().getArgumentsStringList();
                //添加argument
                if (null != sizes && sizes.size()>0) {
                    tempColumn.setColumnArguments(sizes.get(0));
                }
                //设置特殊参数
                if (null != columnDataType.getColumnSpecs() && columnDataType.getColumnSpecs().size()>0) {
                    tempColumn.setColumnSpecs(columnDataType.getColumnSpecs()
                            .stream().map(String::toUpperCase).collect(Collectors.toList()));
                }
                    addList.add(tempColumn);
            }
            if ("DROP".equals(alterColumnType)) {
                ColumnInfoEntity tempColumn = new ColumnInfoEntity();
                tempColumn.setColumnName(expression.getColumnName());
                dropList.add(tempColumn);
            }
            if ("ALTER".equals(alterColumnType)) {
                AlterExpression.ColumnDataType columnDataType = expression.getColDataTypeList().get(0);
                ColumnInfoEntity tempColumn = new ColumnInfoEntity();
                //添加columnName
                tempColumn.setColumnName(columnDataType.getColumnName());

                //添加type
                tempColumn.setDataType(columnDataType.getColDataType().getDataType());
                List<String> arguments = columnDataType.getColDataType().getArgumentsStringList();
                //添加argument
                if (null != arguments) {
                    tempColumn.setColumnArguments(arguments.get(0));
                }
                //设置特殊参数
                if (null != columnDataType.getColumnSpecs() && columnDataType.getColumnSpecs().size()>0) {
                    tempColumn.setColumnSpecs(columnDataType.getColumnSpecs()
                            .stream().map(String::toUpperCase).collect(Collectors.toList()));
                }
                alterList.add(tempColumn);
            }

        }

        entity.setAddColumnList(addList);
        entity.setAlterColumnList(alterList);
        entity.setDropColumnList(dropList);
        return ddlOperate.alterTable(entity);
    }

    public OperateResult sqlMapToDrop(Drop drop) {
        DropEntity dropEntity = new DropEntity();
        String tableName = drop.getName().getName();
        dropEntity.setTableName(tableName);
        return ddlOperate.dropTable(dropEntity);
    }

    public OperateResult sqlMapToCreateIndex(CreateIndex createIndex) {
        CreateIndexMiddleEntity entity = new CreateIndexMiddleEntity();
        Index index = createIndex.getIndex();
        entity.setTableName(createIndex.getTable().getName());
        entity.setIndexName(index.getName());
        if (null != index.getType()) {
            entity.setIndexType(index.getType());
        }
        if (null != index.getColumns()) {
            List<String> columnList = new ArrayList<>();
            for (Index.ColumnParams column : index.getColumns()) {
                columnList.add(column.getColumnName());
            }
            entity.setColumnList(columnList);
        }
        OperateResult result = ddlOperate.createIndex(entity);
        return result;
    }

    public OperateResult sqlMapToDropIndex(Drop drop) {
        DropIndexEntity entity = new DropIndexEntity();
        entity.setIndexName(drop.getName().getName());
        OperateResult result = ddlOperate.dropIndex(entity);
        return  result;
    }

}
