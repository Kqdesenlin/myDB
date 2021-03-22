package com.domain.service;

import com.application.service.PackToDto;
import com.application.service.PackToEntity;
import com.domain.Entity.CreateTempEntity;
import com.domain.Entity.createTable.ColumnInfoEntity;
import com.domain.Entity.createTable.CreateTableEntity;
import com.domain.Entity.createTable.TableInfoEntity;
import com.domain.Entity.result.OperateResult;
import com.domain.event.DDLOperate;
import com.domain.event.DMLOperate;
import com.domain.event.sqlParser.SelectParser;
import com.interfaces.dto.ResultDto;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.logging.Logger;

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

        }
        if (statement instanceof Update){
        }
        if (statement instanceof Delete){
        }
        if (statement instanceof CreateTable) {
            return sqlMapToCreateTable((CreateTable) statement);
        }
        List<String> tableLists = selectParser.getSelectTables(sql);
        CreateTempEntity createTempEntity = PackToEntity.packFromToWhereToCreateTempEntity(tableLists);
        OperateResult operateResult = ddlOperate.createTempTable(createTempEntity);

        return PackToDto.ResultToDto(operateResult);
    }

    public ResultDto sqlMapToCreateTable(CreateTable createTable) {
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

    public ResultDto sqlMapToInsert() {
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
