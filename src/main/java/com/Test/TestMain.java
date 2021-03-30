package com.Test;

import com.domain.Entity.result.OperateResult;
import com.domain.service.SqlToEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMain {
    public static void main(String[] args)throws Exception{
        String createSql = "CREATE TABLE new_table (" +
                "   id int NOT NULL," +
                "column2 varchar(10) not null unique" +
                ")";
        SqlToEntity sqlToEntity = new SqlToEntity();
        OperateResult createResult = sqlToEntity.sqlMapToDML(createSql);
        log.info(createResult.toString());
        String insertSql1 = "insert into new_table values(1,'abc');";
        log.info(sqlToEntity.sqlMapToDML(insertSql1).toString());
        String insertSql2 = "insert into new_table(id,column2) values(2,'abcd');";
        log.info(sqlToEntity.sqlMapToDML(insertSql2).toString());
        String insertSql3 = "insert into new_table(column2,id) values('abcde',3)";
        log.info(sqlToEntity.sqlMapToDML(insertSql3).toString());
        String selectSql1 = "select * from new_table";

    }
}
